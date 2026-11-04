
void FUN_ffff000090769a9c(long param_1)

{
  int iVar1;
  ulong uVar2;
  ulong uVar3;
  ulong uVar4;
  long lVar5;

  if (param_1 != 0) {
    uVar3 = *(ulong *)(param_1 + 0x30);
    FUN_ffff00009077ea84(s__AVB__Rollback_index__d____0x_ll_ffff0000907cee40,0,uVar3);
    uVar4 = *(ulong *)(param_1 + 0x30);
    if (uVar3 <= uVar4 || uVar4 == 0xffffffff) {
      uVar4 = uVar3;
    }
    FUN_ffff00009077ea84
              (s__AVB__Rollback_index__d____0x_ll_ffff0000907cee40,1,*(undefined8 *)(param_1 + 0x38)
              );
    lVar5 = 0;
    uVar3 = *(ulong *)(param_1 + 0x38);
    do {
      FUN_ffff00009077ea84
                (s__AVB__Rollback_index__d____0x_ll_ffff0000907cee40,(int)lVar5 + 2,
                 *(undefined8 *)(param_1 + 0x40 + lVar5 * 8));
      uVar2 = *(ulong *)(param_1 + 0x40 + lVar5 * 8);
      lVar5 = lVar5 + 1;
      if (uVar4 <= uVar2 || uVar2 == 0xffffffff) {
        uVar2 = uVar4;
      }
      uVar4 = uVar2;
    } while (lVar5 != 0x1e);
    if ((uVar3 < 0x40) && (iVar1 = thunk_FUN_ffff0000907ad0c8(3,uVar3 & 0xffffffff), iVar1 != 0)) {
      FUN_ffff00009077ea84(s__AVB__Failed_to_update_recovery_g_ffff0000907d6fc8,iVar1);
    }
    if ((uVar2 < 0x40) && (iVar1 = thunk_FUN_ffff0000907ad0c8(2,uVar2 & 0xffffffff), iVar1 != 0)) {
      FUN_ffff00009077ea84(s__AVB__Failed_to_update_avb_group_ffff0000907e2793,iVar1);
      return;
    }
  }
  return;
}
