// ffff000090768fd0  size=168  FUN_ffff000090768fd0

void FUN_ffff000090768fd0(long param_1)

{
  int iVar1;
  long lVar2;
  ulong uVar3;
  ulong uVar4;
  
  if (param_1 != 0) {
    lVar2 = 0;
    uVar4 = *(ulong *)(param_1 + 0x30);
    do {
      uVar3 = *(ulong *)(param_1 + 0x40 + lVar2);
      lVar2 = lVar2 + 8;
      if (uVar4 <= uVar3 || uVar3 == 0xffffffff) {
        uVar3 = uVar4;
      }
      uVar4 = uVar3;
    } while (lVar2 != 0xf0);
    if ((*(ulong *)(param_1 + 0x38) < 0x40) && (iVar1 = thunk_FUN_ffff0000907acaf8(3), iVar1 != 0))
    {
      FUN_ffff00009077de54(s__AVB__Failed_to_update_recovery_g_ffff0000907dea12,iVar1);
    }
    if ((uVar3 < 0x40) && (iVar1 = thunk_FUN_ffff0000907acaf8(2,uVar3 & 0xffffffff), iVar1 != 0)) {
      FUN_ffff00009077de54(s__AVB__Failed_to_update_avb_group_ffff0000907e9d03,iVar1);
      return;
    }
  }
  return;
}


