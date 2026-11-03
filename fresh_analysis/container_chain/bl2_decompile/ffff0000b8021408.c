
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b8021408(void)

{
  uint uVar1;
  long lVar2;
  undefined8 uVar3;
  
  lVar2 = FUN_ffff0000b800c9f4
                    (0x1000000,0x1000000,0x90000000,0,0,s_scp_aov_reserved_ffff0000b8092652);
  if (lVar2 != 0) {
    _DAT_ffff00001cf21054 = _DAT_ffff00001cf21054 | 1;
    uVar3 = FUN_ffff0000b80263f0(1,4);
    FUN_ffff0000b802634c(uVar3,0x1692,0,1,3);
    FUN_ffff0000b802634c(uVar3,0x1695,0,1,3);
    FUN_ffff0000b802634c(uVar3,0x168f,1,1,3);
    uVar1 = thunk_FUN_ffff0000b802ce0c(0x12a);
    if ((uVar1 >> 6 & 1) != 0) {
      _DAT_ffff00001cf2b034 = _DAT_ffff00001cf2b034 & 0xffffffc0 | uVar1 & 0x3f;
      _DAT_ffff00001cf2a048 = uVar1;
    }
    return;
  }
  FUN_ffff0000b804c33c(s__SCP__aov_mblock_reserve_fail_ffff0000b809644e);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_common_scp_RV__ffff0000b80a1095,0x34b,&DAT_ffff0000b8086efb);
}


