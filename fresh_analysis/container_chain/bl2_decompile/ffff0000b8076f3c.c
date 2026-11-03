
void FUN_ffff0000b8076f3c(void)

{
  undefined1 *puVar1;
  int iVar2;
  char *pcVar3;
  undefined1 uVar4;
  long lVar5;
  byte local_34 [4];
  
  FUN_ffff0000b804c33c(s__SPMI___s_ffff0000b809a51e,s_spmi_test_ffff0000b809f0c1);
  lVar5 = 0;
  do {
    puVar1 = &DAT_ffff0000b81078e8 + lVar5;
    local_34[0] = 0;
    iVar2 = FUN_ffff0000b80262bc
                      (puVar1,*(undefined2 *)((long)&DAT_ffff0000b81078f4 + lVar5),local_34,1);
    if (iVar2 != 0) {
      FUN_ffff0000b804c33c
                (s__SPMI___s_fail__spmi_ext_registe_ffff0000b808c156,
                 s_spmi_read_check_ffff0000b808f738);
    }
    pcVar3 = s__SPMI___s_pass__slvid__d_rdata___ffff0000b808e7a7;
    if ((&DAT_ffff0000b81078f7)[lVar5] == 0) {
      uVar4 = *puVar1;
      if (local_34[0] != (&DAT_ffff0000b81078f6)[lVar5]) goto LAB_ffff0000b8076f9c;
    }
    else {
      uVar4 = *puVar1;
      if ((((&DAT_ffff0000b81078f6)[lVar5] ^ local_34[0]) & (&DAT_ffff0000b81078f7)[lVar5]) != 0) {
LAB_ffff0000b8076f9c:
        pcVar3 = s__SPMI___s_fail__slvid__d_rdata___ffff0000b809140e;
      }
    }
    FUN_ffff0000b804c33c(pcVar3,s_spmi_read_check_ffff0000b808f738,uVar4);
    lVar5 = lVar5 + 0x30;
    if (lVar5 == 0x180) {
      return;
    }
  } while( true );
}


