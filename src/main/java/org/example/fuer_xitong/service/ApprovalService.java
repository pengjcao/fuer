package org.example.fuer_xitong.service;

public interface ApprovalService {
     boolean handleApproval(String piId, int pi_info_id,String approverId, Integer role, Boolean approve, String comment);
}
