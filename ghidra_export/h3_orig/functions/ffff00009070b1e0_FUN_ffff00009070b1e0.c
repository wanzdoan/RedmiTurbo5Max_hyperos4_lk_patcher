// ffff00009070b1e0  size=252  FUN_ffff00009070b1e0

void FUN_ffff00009070b1e0(undefined8 param_1)

{
  long lVar1;
  long lVar2;
  undefined8 uVar3;
  long lVar4;
  char *pcVar5;
  
  lVar1 = FUN_ffff00009077bd60(&DAT_ffff0000907dbe5c);
  if (lVar1 == 0) {
    pcVar5 = s_open_misc_fail_ffff0000907faf18;
  }
  else {
    lVar2 = FUN_ffff00009077d24c(0x800);
    if (lVar2 == 0) {
      FUN_ffff000090709d10(s_allocate_misc_msg_fail_ffff00009080020d);
      FUN_ffff00009077bce4(lVar1);
      return;
    }
    FUN_ffff00009077eb88(lVar2,0,0x800);
    uVar3 = FUN_ffff00009077ef88(param_1);
    FUN_ffff00009077efe4(lVar2,param_1,uVar3);
    lVar4 = FUN_ffff00009077be7c(lVar1,lVar2,0,0x800);
    FUN_ffff00009077bce4(lVar1);
    thunk_FUN_ffff0000907c2ad8(lVar2);
    if (0 < lVar4) {
      FUN_ffff000090709d50(&DAT_ffff0000907df295);
      FUN_ffff000090745d40(2);
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009072259c(1,7);
    }
    pcVar5 = s_bio_write_failed_ffff0000907d0a4a;
  }
  FUN_ffff000090709d10(pcVar5);
  return;
}


