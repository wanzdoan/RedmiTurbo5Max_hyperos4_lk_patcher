// ffff000090787370  size=352  FUN_ffff000090787370

undefined8 FUN_ffff000090787370(void)

{
  char cVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  long lVar5;
  char local_58 [8];
  char acStack_50 [8];
  char local_48 [8];
  undefined7 uStack_40;
  undefined4 local_39;
  char local_30 [6];
  char acStack_2a [2];
  char acStack_28 [8];
  
  local_30 = (char  [6])s_boot_recovery_ffff0000907d8bd1._0_6_;
  acStack_2a = (char  [2])s_boot_recovery_ffff0000907d8bd1._6_2_;
  acStack_28._0_6_ = s_boot_recovery_ffff0000907d8bd1._8_6_;
  local_58 = (char  [8])s_recovery___reason_MisecidsInvali_ffff0000907db842._0_8_;
  acStack_50 = (char  [8])s_recovery___reason_MisecidsInvali_ffff0000907db842._8_8_;
  local_48 = (char  [8])s_recovery___reason_MisecidsInvali_ffff0000907db842._16_8_;
  uStack_40 = (undefined7)s_recovery___reason_MisecidsInvali_ffff0000907db842._24_8_;
  local_39 = 0xa6469;
  lVar3 = FUN_ffff00009077d24c(0x800);
  if (lVar3 == 0) {
    FUN_ffff00009077de54(s__LK_RECOVERY__allocate_misc_msg_f_ffff0000907e6bcc);
    uVar4 = 0xffffffea;
  }
  else {
    FUN_ffff00009077eb88(lVar3,0,0x800);
    uVar4 = FUN_ffff00009077ef88(local_30);
    FUN_ffff00009077efe4(lVar3,local_30,uVar4);
    uVar4 = FUN_ffff00009077ef88(local_58);
    FUN_ffff00009077efe4(lVar3 + 0x40,local_58,uVar4);
    lVar5 = FUN_ffff00009077bd60(&DAT_ffff0000907dbe45);
    if (lVar5 == 0) {
      FUN_ffff00009077de54(s__LK_RECOVERY__open_misc_fail_ffff0000907e3a23);
    }
    else {
      iVar2 = FUN_ffff00009077be7c(lVar5,lVar3,0,0x800);
      if (iVar2 < 1) {
        FUN_ffff00009077de54(s__LK_RECOVERY__write_bootloader_m_ffff0000907dd389);
      }
      FUN_ffff00009077bce4(lVar5);
    }
    thunk_FUN_ffff0000907c2ad8(lVar3);
    FUN_ffff000090745d40(2);
    cVar1 = FUN_ffff000090710b48();
    if (cVar1 != '\x02') {
      FUN_ffff00009077de54(s_Error___Not_in_recovery_mode__Co_ffff0000907f1e12);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009072259c(1,1);
    }
    FUN_ffff00009077de54(s_In_recovery_mode__Continue_ffff0000907d02bc);
    uVar4 = 0;
  }
  return uVar4;
}


