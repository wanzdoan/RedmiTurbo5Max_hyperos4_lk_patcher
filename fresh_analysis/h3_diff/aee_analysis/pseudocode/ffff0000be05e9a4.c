
void FUN_ffff0000be05e9a4(long *param_1)

{
  long *plVar1;
  char cVar2;
  bool bVar3;
  int iVar4;
  
  FUN_ffff0000be01a730(&DAT_ffff0000be0ef2e8,0xffffffff);
  plVar1 = (long *)param_1[1];
  *plVar1 = *param_1;
  *(long **)(*param_1 + 8) = plVar1;
  *param_1 = 0;
  param_1[1] = 0;
  FUN_ffff0000be01a7c8(&DAT_ffff0000be0ef2e8);
  plVar1 = param_1 + 2;
  do {
    iVar4 = (int)*plVar1 + -1;
    cVar2 = '\x01';
    bVar3 = (bool)ExclusiveMonitorPass(plVar1,0x10);
    if (bVar3) {
      *(int *)plVar1 = iVar4;
      cVar2 = ExclusiveMonitorsStatus();
    }
  } while (cVar2 != '\0');
  if (iVar4 == 0) {
    FUN_ffff0000be05fd98
              (s__s__d__last_ref__removing___s__ffff0000be0b91d9,s_bdev_dec_ref_ffff0000be0b352c,
               0x146,param_1[3]);
    if ((code *)param_1[0x14] != (code *)0x0) {
      (*(code *)param_1[0x14])(param_1);
    }
    thunk_FUN_ffff0000be09937c(param_1[3]);
    return;
  }
  return;
}


