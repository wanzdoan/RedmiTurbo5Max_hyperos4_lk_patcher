// ffff0000907bbd74  size=248  FUN_ffff0000907bbd74

void FUN_ffff0000907bbd74(void)

{
  undefined1 *puVar1;
  int iVar2;
  char *pcVar3;
  undefined1 uVar4;
  long lVar5;
  byte local_34 [4];
  
  FUN_ffff00009077e11c(s__SPMI___s_ffff0000907f5ab2,s_spmi_test_ffff0000907fe68e);
  lVar5 = 0;
  do {
    puVar1 = &DAT_ffff0000908380c8 + lVar5;
    local_34[0] = 0;
    iVar2 = FUN_ffff00009074046c
                      (puVar1,*(undefined2 *)((long)&DAT_ffff0000908380d4 + lVar5),local_34,1);
    if (iVar2 != 0) {
      FUN_ffff00009077e11c
                (s__SPMI___s_fail__spmi_ext_registe_ffff0000907dbd3b,
                 s_spmi_read_check_ffff0000907e232f);
    }
    pcVar3 = s__SPMI___s_pass__slvid__d_rdata___ffff0000907e07b5;
    if ((&DAT_ffff0000908380d7)[lVar5] == 0) {
      uVar4 = *puVar1;
      if (local_34[0] != (&DAT_ffff0000908380d6)[lVar5]) goto LAB_ffff0000907bbdd4;
    }
    else {
      uVar4 = *puVar1;
      if ((((&DAT_ffff0000908380d6)[lVar5] ^ local_34[0]) & (&DAT_ffff0000908380d7)[lVar5]) != 0) {
LAB_ffff0000907bbdd4:
        pcVar3 = s__SPMI___s_fail__slvid__d_rdata___ffff0000907e591a;
      }
    }
    FUN_ffff00009077e11c(pcVar3,s_spmi_read_check_ffff0000907e232f,uVar4);
    lVar5 = lVar5 + 0x30;
    if (lVar5 == 0x180) {
      return;
    }
  } while( true );
}


