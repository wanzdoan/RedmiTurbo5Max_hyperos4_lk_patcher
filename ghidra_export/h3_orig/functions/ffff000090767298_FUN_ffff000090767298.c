// ffff000090767298  size=188  FUN_ffff000090767298

undefined8 FUN_ffff000090767298(long param_1)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined8 uVar4;
  
  if (param_1 == 0) {
    pcVar3 = s__s__d__misc_msg_is_NULL_ffff0000907e9cba;
    uVar4 = 0xc21;
  }
  else {
    lVar2 = FUN_ffff00009077bd60(&DAT_ffff0000907dbe5c);
    if (lVar2 != 0) {
      iVar1 = FUN_ffff00009077bdf4(lVar2,param_1,0,0x800);
      if (iVar1 < 1) {
        FUN_ffff00009077de54
                  (s__s__d__read_bootloader_message_f_ffff0000907dd13e,
                   s_read_bcb_from_misc_partition_ffff0000907f34b8,0xc2d);
        FUN_ffff00009077bce4(lVar2);
        return 0;
      }
      FUN_ffff00009077bce4(lVar2);
      return 1;
    }
    pcVar3 = s__s__d__open_misc_fail_ffff0000907dd127;
    uVar4 = 0xc27;
  }
  FUN_ffff00009077de54(pcVar3,s_read_bcb_from_misc_partition_ffff0000907f34b8,uVar4);
  return 0;
}


