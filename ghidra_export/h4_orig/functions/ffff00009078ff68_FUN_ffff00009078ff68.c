// ffff00009078ff68  size=208  FUN_ffff00009078ff68

bool FUN_ffff00009078ff68(long param_1,uint *param_2)

{
  undefined8 uVar1;
  long lVar2;
  long lVar3;
  uint uVar4;
  ulong uVar5;
  ulong uVar6;
  
  lVar2 = FUN_ffff0000907168b0();
  if (param_2 != (uint *)0x0) {
    while( true ) {
      uVar1 = daif;
      uVar4 = (uint)uVar1;
      if ((uVar4 >> 7 & 1) == 0) {
        uVar5 = daif;
        daif = uVar5 | 0x80;
      }
      uVar5 = *(ulong *)(param_1 + 0x80);
      uVar6 = (ulong)(1 << (ulong)(*(byte *)(param_1 + 0x178) & 0x1f));
      if ((uVar5 & uVar6) == 0) break;
      *(undefined8 *)(param_1 + 0x48) = 0;
      if ((uVar4 >> 7 & 1) == 0) {
        uVar5 = daif;
        daif = uVar5 & 0xffffffffffffff7f;
      }
      lVar3 = FUN_ffff0000907168b0();
      if (5000000 < (ulong)(lVar3 - lVar2)) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_ufs_ufs_ffff0000907d6e37,0x353,&DAT_ffff0000907d2506);
      }
    }
    *param_2 = (uint)*(byte *)(param_1 + 0x178);
    *(undefined8 *)(param_1 + 0x48) = 0;
    *(ulong *)(param_1 + 0x80) = uVar5 | uVar6;
    if ((uVar4 >> 7 & 1) == 0) {
      uVar5 = daif;
      daif = uVar5 & 0xffffffffffffff7f;
    }
  }
  return param_2 != (uint *)0x0;
}


