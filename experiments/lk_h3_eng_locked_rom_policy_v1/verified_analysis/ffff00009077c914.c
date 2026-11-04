
void FUN_ffff00009077c914(long param_1)

{
  int *piVar1;
  int iVar2;
  char cVar3;
  bool bVar4;

  piVar1 = (int *)(param_1 + 0x10);
  do {
    iVar2 = *piVar1;
    cVar3 = '\x01';
    bVar4 = (bool)ExclusiveMonitorPass(piVar1,0x10);
    if (bVar4) {
      *piVar1 = iVar2 + -1;
      cVar3 = ExclusiveMonitorsStatus();
    }
  } while (cVar3 != '\0');
  if (iVar2 + -1 == 0) {
    FUN_ffff00009077ea84
              (s__s__d__last_ref__removing___s__ffff0000907e786f,s_bdev_dec_ref_ffff0000907df406,
               0x146,*(undefined8 *)(param_1 + 0x18));
    if (*(code **)(param_1 + 0xa0) != (code *)0x0) {
      (**(code **)(param_1 + 0xa0))(param_1);
    }
    thunk_FUN_ffff0000907c310c(*(undefined8 *)(param_1 + 0x18));
    return;
  }
  return;
}
