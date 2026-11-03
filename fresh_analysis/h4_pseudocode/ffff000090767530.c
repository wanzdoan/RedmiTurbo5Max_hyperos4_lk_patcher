
undefined8 FUN_ffff000090767530(long param_1)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined8 uVar4;
  
  if (param_1 == 0) {
    pcVar3 = s__s__d__misc_msg_is_NULL_ffff0000907e9dfe;
    uVar4 = 0xc21;
  }
  else {
    lVar2 = FUN_ffff00009077c028(&DAT_ffff0000907dbf67);
    if (lVar2 != 0) {
      iVar1 = FUN_ffff00009077c0bc(lVar2,param_1,0,0x800);
      if (iVar1 < 1) {
        FUN_ffff00009077e11c
                  (s__s__d__read_bootloader_message_f_ffff0000907dd214,
                   s_read_bcb_from_misc_partition_ffff0000907f3644,0xc2d);
        FUN_ffff00009077bfac(lVar2);
        return 0;
      }
      FUN_ffff00009077bfac(lVar2);
      return 1;
    }
    pcVar3 = s__s__d__open_misc_fail_ffff0000907dd1fd;
    uVar4 = 0xc27;
  }
  FUN_ffff00009077e11c(pcVar3,s_read_bcb_from_misc_partition_ffff0000907f3644,uVar4);
  return 0;
}


