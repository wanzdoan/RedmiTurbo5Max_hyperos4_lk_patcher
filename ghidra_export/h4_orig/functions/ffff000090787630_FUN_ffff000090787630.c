// ffff000090787630  size=352  FUN_ffff000090787630

undefined8 FUN_ffff000090787630(void)

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
  
  local_30 = (char  [6])s_boot_recovery_ffff0000907d8c68._0_6_;
  acStack_2a = (char  [2])s_boot_recovery_ffff0000907d8c68._6_2_;
  acStack_28._0_6_ = s_boot_recovery_ffff0000907d8c68._8_6_;
  local_58 = (char  [8])s_recovery___reason_MisecidsInvali_ffff0000907db964._0_8_;
  acStack_50 = (char  [8])s_recovery___reason_MisecidsInvali_ffff0000907db964._8_8_;
  local_48 = (char  [8])s_recovery___reason_MisecidsInvali_ffff0000907db964._16_8_;
  uStack_40 = (undefined7)s_recovery___reason_MisecidsInvali_ffff0000907db964._24_8_;
  local_39 = 0xa6469;
  lVar3 = FUN_ffff00009077d514(0x800);
  if (lVar3 == 0) {
    FUN_ffff00009077e11c(s__LK_RECOVERY__allocate_misc_msg_f_ffff0000907e6d27);
    uVar4 = 0xffffffea;
  }
  else {
    FUN_ffff00009077ee50(lVar3,0,0x800);
    uVar4 = FUN_ffff00009077f250(local_30);
    FUN_ffff00009077f2ac(lVar3,local_30,uVar4);
    uVar4 = FUN_ffff00009077f250(local_58);
    FUN_ffff00009077f2ac(lVar3 + 0x40,local_58,uVar4);
    lVar5 = FUN_ffff00009077c028(&DAT_ffff0000907dbf67);
    if (lVar5 == 0) {
      FUN_ffff00009077e11c(s__LK_RECOVERY__open_misc_fail_ffff0000907e3b0b);
    }
    else {
      iVar2 = FUN_ffff00009077c144(lVar5,lVar3,0,0x800);
      if (iVar2 < 1) {
        FUN_ffff00009077e11c(s__LK_RECOVERY__write_bootloader_m_ffff0000907dd443);
      }
      FUN_ffff00009077bfac(lVar5);
    }
    thunk_FUN_ffff0000907c314c(lVar3);
    FUN_ffff000090745fd8(2);
    cVar1 = FUN_ffff000090710bc0();
    if (cVar1 != '\x02') {
      FUN_ffff00009077e11c(s_Error___Not_in_recovery_mode__Co_ffff0000907f1fb5);
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000907227e0(1,1);
    }
    FUN_ffff00009077e11c(s_In_recovery_mode__Continue_ffff0000907d02db);
    uVar4 = 0;
  }
  return uVar4;
}


