
undefined8 FUN_ffff0000b80163ec(void)

{
  int iVar1;
  long lVar2;
  long lVar3;
  undefined8 uVar4;
  
  DAT_ffff0000b811b0d0 = FUN_ffff0000b804bccc(0x1000);
  FUN_ffff0000b804d070(DAT_ffff0000b811b0d0,0,0x1000);
  lVar3 = DAT_ffff0000b811b0d0;
  lVar2 = FUN_ffff0000b804a99c(s_gsort_ffff0000b8099a60);
  if (lVar2 == 0) {
    FUN_ffff0000b804c33c(s_Parttion__gsort__is_not_exist__ffff0000b80924be);
  }
  else {
    lVar3 = FUN_ffff0000b804aa30(lVar2,lVar3,0xe00000,0x1000);
    if (lVar3 == 0x1000) {
      FUN_ffff0000b804c33c(s_gsort_read_size___zu_ffff0000b8097e3b,0x1000);
    }
    else {
      FUN_ffff0000b804c33c(s_Read_gsort_size__ld_is_not_equal_ffff0000b8099a66,lVar3,0x1000);
    }
    FUN_ffff0000b804a920(lVar2);
  }
  FUN_ffff0000b804c33c
            (s_fatal_error_magic__s_count__d_ffff0000b8089a6b,DAT_ffff0000b811b0d0,
             *(undefined1 *)(DAT_ffff0000b811b0d0 + 0x10));
  lVar3 = DAT_ffff0000b811b0d0;
  uVar4 = FUN_ffff0000b804d3cc(s_fatal_error_ffff0000b80924b2);
  iVar1 = FUN_ffff0000b804d4c4(lVar3,s_fatal_error_ffff0000b80924b2,uVar4);
  if ((iVar1 == 0) && (*(char *)(lVar3 + 0x10) != '\0')) {
    FUN_ffff0000b804c33c(s_fatal_error_is_set_ffff0000b8090936);
    uVar4 = 1;
  }
  else {
    FUN_ffff0000b804c33c(s_fatal_error_is_not_set_ffff0000b8096fec);
    thunk_FUN_ffff0000b8082558(DAT_ffff0000b811b0d0);
    uVar4 = 0;
  }
  return uVar4;
}


