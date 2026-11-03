// ffff0000907bb700  size=248  FUN_ffff0000907bb700

void FUN_ffff0000907bb700(void)

{
  undefined1 *puVar1;
  int iVar2;
  char *pcVar3;
  undefined1 uVar4;
  long lVar5;
  byte local_34 [4];
  
  FUN_ffff00009077de54(s__SPMI___s_ffff0000907f5924,s_spmi_test_ffff0000907fe4e6);
  lVar5 = 0;
  do {
    puVar1 = &DAT_ffff0000908380a8 + lVar5;
    local_34[0] = 0;
    iVar2 = FUN_ffff0000907401d4
                      (puVar1,*(undefined2 *)((long)&DAT_ffff0000908380b4 + lVar5),local_34,1);
    if (iVar2 != 0) {
      FUN_ffff00009077de54
                (s__SPMI___s_fail__spmi_ext_registe_ffff0000907dbc19,
                 s_spmi_read_check_ffff0000907e2247);
    }
    pcVar3 = s__SPMI___s_pass__slvid__d_rdata___ffff0000907e06fb;
    if ((&DAT_ffff0000908380b7)[lVar5] == 0) {
      uVar4 = *puVar1;
      if (local_34[0] != (&DAT_ffff0000908380b6)[lVar5]) goto LAB_ffff0000907bb760;
    }
    else {
      uVar4 = *puVar1;
      if ((((&DAT_ffff0000908380b6)[lVar5] ^ local_34[0]) & (&DAT_ffff0000908380b7)[lVar5]) != 0) {
LAB_ffff0000907bb760:
        pcVar3 = s__SPMI___s_fail__slvid__d_rdata___ffff0000907e57ff;
      }
    }
    FUN_ffff00009077de54(pcVar3,s_spmi_read_check_ffff0000907e2247,uVar4);
    lVar5 = lVar5 + 0x30;
    if (lVar5 == 0x180) {
      return;
    }
  } while( true );
}


