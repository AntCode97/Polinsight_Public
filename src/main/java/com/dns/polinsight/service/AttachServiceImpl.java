package com.dns.polinsight.service;

import com.dns.polinsight.domain.Attach;
import com.dns.polinsight.domain.Board;
import com.dns.polinsight.domain.BoardDTO;
import com.dns.polinsight.exception.AttachNotFoundException;
import com.dns.polinsight.exception.BoardNotFoundException;
import com.dns.polinsight.repository.AttachRepository;
import com.dns.polinsight.repository.BoardRepository;
import com.dns.polinsight.storage.StorageService;
import com.sun.tools.attach.AttachNotSupportedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttachServiceImpl implements AttachService{

    private final AttachRepository repository;
    private final BoardRepository boardRepository;
    private final StorageService storageService;
    private final BoardServiceImpl boardService;

    @Override
    public List<Attach> findAll() {
        return repository.findAll();
    }

    public List<File> findFiles(Long boardId){

            Optional<Board> boards = boardRepository.findById(boardId);
            Board board = boards.get();
            System.out.println(board.getId() + board.getTitle());
            List<Attach> attaches = repository.findByBoardId(boardId);

            List<File> files = new ArrayList<>();

            for (Attach attach: attaches) {
                files.add(new File(attach.getFilePath()));
        }
            return files;
    }


    @Override
    public Attach find(Attach attach) {
        return repository.findById(attach.getId()).orElseThrow(AttachNotFoundException::new);

    }

    @Override
    public Attach saveOrUpdate(Attach attach) {
        return repository.save(attach);
    }

    public void addAttach(BoardDTO boardDTO) {
        List<MultipartFile> files = boardDTO.getFiles();
        if(!files.isEmpty()){
            Board board = Board.builder(boardDTO).build();
            List<Attach> attaches = new ArrayList<>();
            for (MultipartFile file: files) {
                if (!file.isEmpty()){
                    Attach attach = new Attach().builder().filename(file.getOriginalFilename())
                            .fileSize(file.getSize())
                            .originalName(file.getOriginalFilename())
                            .filePath("./upload-dir/"+file.getOriginalFilename())
                            .board(board).build();
                    attaches.add(attach);
                    repository.save(attach);
                    storageService.store(file);
                }

            }
            boardDTO.setAttaches(attaches);
            boardService.addBoard(boardDTO);

        }


    }

}
