
void FUN_ffff0000be095b98(void)

{
  undefined1 *puVar1;
  int iVar2;
  char *pcVar3;
  undefined1 uVar4;
  long lVar5;
  byte local_34 [4];
  
  FUN_ffff0000be05fd98(s__SPMI___s_ffff0000be0bdde7,s_spmi_test_ffff0000be0c394b);
  lVar5 = 0;
  do {
    puVar1 = &DAT_ffff0000be0f42a0 + lVar5;
    local_34[0] = 0;
    iVar2 = FUN_ffff0000be0302b4
                      (puVar1,*(undefined2 *)((long)&DAT_ffff0000be0f42ac + lVar5),local_34,1);
    if (iVar2 != 0) {
      FUN_ffff0000be05fd98
                (s__SPMI___s_fail__spmi_ext_registe_ffff0000be0ac18d,
                 s_spmi_read_check_ffff0000be0b06f0);
    }
    pcVar3 = s__SPMI___s_pass__slvid__d_rdata___ffff0000be0af2c4;
    if ((&DAT_ffff0000be0f42af)[lVar5] == 0) {
      uVar4 = *puVar1;
      if (local_34[0] != (&DAT_ffff0000be0f42ae)[lVar5]) goto LAB_ffff0000be095bf8;
    }
    else {
      uVar4 = *puVar1;
      if ((((&DAT_ffff0000be0f42ae)[lVar5] ^ local_34[0]) & (&DAT_ffff0000be0f42af)[lVar5]) != 0) {
LAB_ffff0000be095bf8:
        pcVar3 = s__SPMI___s_fail__slvid__d_rdata___ffff0000be0b29ca;
      }
    }
    FUN_ffff0000be05fd98(pcVar3,s_spmi_read_check_ffff0000be0b06f0,uVar4);
    lVar5 = lVar5 + 0x30;
    if (lVar5 == 0x180) {
      return;
    }
  } while( true );
}


