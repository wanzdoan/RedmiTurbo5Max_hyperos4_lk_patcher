// ffff000090792c90  size=284  FUN_ffff000090792c90

int FUN_ffff000090792c90(long param_1)

{
  undefined8 uVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  long lVar5;
  uint uVar6;
  ulong uVar7;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  lVar4 = FUN_ffff00009071676c();
  while( true ) {
    uVar1 = daif;
    uVar6 = (uint)uVar1;
    if ((uVar6 >> 7 & 1) == 0) {
      uVar7 = daif;
      daif = uVar7 | 0x80;
    }
    uVar7 = (ulong)(1 << (ulong)(*(byte *)(param_1 + 0x178) & 0x1f));
    if ((*(ulong *)(param_1 + 0x80) & uVar7) == 0) break;
    *(undefined8 *)(param_1 + 0x48) = 0;
    if ((uVar6 >> 7 & 1) == 0) {
      uVar7 = daif;
      daif = uVar7 & 0xffffffffffffff7f;
    }
    lVar5 = FUN_ffff00009071676c();
    if (5000000 < (ulong)(lVar5 - lVar4)) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_common_ufs_ufs_ffff0000907d6da1,0x353,&DAT_ffff0000907d24c9);
    }
  }
  *(undefined8 *)(param_1 + 0x48) = 0;
  *(ulong *)(param_1 + 0x80) = *(ulong *)(param_1 + 0x80) | uVar7;
  if ((uVar6 >> 7 & 1) == 0) {
    uVar7 = daif;
    daif = uVar7 & 0xffffffffffffff7f;
  }
  local_40 = 0;
  uStack_38 = 0;
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  FUN_ffff00009078f308(&local_60);
  iVar2 = FUN_ffff000090790d5c(param_1,&local_60);
  iVar3 = 0;
  if ((iVar2 != 0) && (iVar2 = FUN_ffff000090790d5c(param_1,&local_60), iVar3 = 0, iVar2 != 0)) {
    iVar3 = FUN_ffff000090790d5c(param_1,&local_60);
  }
  if (iVar3 != 0) {
    FUN_ffff00009077de54(s__UFS___s_fail_ffff0000907ded3e,s_ufs_poweroff_notify_ffff000090802f4c);
  }
  return iVar3;
}


