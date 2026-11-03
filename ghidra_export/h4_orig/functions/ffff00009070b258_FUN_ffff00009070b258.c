// ffff00009070b258  size=252  FUN_ffff00009070b258

void FUN_ffff00009070b258(undefined8 param_1)

{
  long lVar1;
  long lVar2;
  undefined8 uVar3;
  long lVar4;
  char *pcVar5;
  
  lVar1 = FUN_ffff00009077c028(&DAT_ffff0000907dbf67);
  if (lVar1 == 0) {
    pcVar5 = s_open_misc_fail_ffff0000907fb0b4;
  }
  else {
    lVar2 = FUN_ffff00009077d514(0x800);
    if (lVar2 == 0) {
      FUN_ffff000090709c9c(s_allocate_misc_msg_fail_ffff0000908003b5);
      FUN_ffff00009077bfac(lVar1);
      return;
    }
    FUN_ffff00009077ee50(lVar2,0,0x800);
    uVar3 = FUN_ffff00009077f250(param_1);
    FUN_ffff00009077f2ac(lVar2,param_1,uVar3);
    lVar4 = FUN_ffff00009077c144(lVar1,lVar2,0,0x800);
    FUN_ffff00009077bfac(lVar1);
    thunk_FUN_ffff0000907c314c(lVar2);
    if (0 < lVar4) {
      FUN_ffff000090709cdc(&DAT_ffff0000907df338);
      FUN_ffff000090745fd8(2);
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000907227e0(1,7);
    }
    pcVar5 = s_bio_write_failed_ffff0000907d0a61;
  }
  FUN_ffff000090709c9c(pcVar5);
  return;
}


