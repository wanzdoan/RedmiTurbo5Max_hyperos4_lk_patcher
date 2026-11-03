// ffff0000907cc3b0  size=788  FUN_ffff0000907cc3b0

ulong FUN_ffff0000907cc3b0(long param_1,long *param_2,ulong param_3)

{
  long *plVar1;
  uint uVar2;
  ulong uVar3;
  long lVar4;
  long lVar5;
  long lVar6;
  ulong uVar7;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 local_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  lVar6 = param_3 - 1;
  if (param_3 == 0 || lVar6 == 0) {
    uVar3 = FUN_ffff0000907cbba8(param_1,*param_2);
    return uVar3;
  }
  lVar4 = FUN_ffff00009077d5a8(param_3,0x18);
  lVar5 = lVar4;
  uVar3 = param_3;
  if (lVar4 == 0) {
    return 0xffffb280;
  }
  do {
    FUN_ffff0000907c7040(lVar5);
    uVar3 = uVar3 - 1;
    lVar5 = lVar5 + 0x18;
  } while (uVar3 != 0);
  local_70 = 0;
  uStack_68 = 0;
  local_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_80 = 0;
  local_a8 = 0;
  uStack_a0 = 0;
  local_98 = 0;
  FUN_ffff0000907c7040(&local_78);
  FUN_ffff0000907c7040(&local_90);
  FUN_ffff0000907c7040(&local_a8);
  uVar2 = FUN_ffff0000907c7260(lVar4,*param_2 + 0x30);
  uVar3 = (ulong)uVar2;
  if (uVar2 == 0) {
    lVar5 = lVar4;
    plVar1 = param_2;
    if (1 < param_3) {
      do {
        uVar2 = FUN_ffff0000907c846c(lVar5 + 0x18,lVar5,plVar1[1] + 0x30);
        if ((uVar2 != 0) || (uVar2 = FUN_ffff0000907cc6c4(lVar5 + 0x18,param_1), uVar2 != 0))
        goto LAB_ffff0000907cc658;
        lVar6 = lVar6 + -1;
        lVar5 = lVar5 + 0x18;
        plVar1 = plVar1 + 1;
      } while (lVar6 != 0);
    }
    uVar2 = FUN_ffff0000907c9fa4(&local_78,lVar4 + param_3 * 0x18 + -0x18,param_1 + 8);
    uVar3 = (ulong)uVar2;
    if (uVar2 == 0) {
      lVar6 = lVar4 + param_3 * 0x18 + -0x30;
      uVar7 = param_3;
      do {
        if (uVar7 == 1) {
          uVar2 = FUN_ffff0000907c7260();
        }
        else {
          uVar2 = FUN_ffff0000907c846c(&local_90,&local_78,lVar6);
          if (((uVar2 != 0) || (uVar2 = FUN_ffff0000907cc6c4(&local_90,param_1), uVar2 != 0)) ||
             (uVar2 = FUN_ffff0000907c846c(&local_78,&local_78,param_2[uVar7 - 1] + 0x30),
             uVar2 != 0)) goto LAB_ffff0000907cc658;
          uVar2 = FUN_ffff0000907cc6c4(&local_78,param_1);
        }
        if ((((uVar2 != 0) ||
             (uVar2 = FUN_ffff0000907c846c(&local_a8,&local_90,&local_90), uVar2 != 0)) ||
            ((uVar2 = FUN_ffff0000907cc6c4(&local_a8,param_1), uVar2 != 0 ||
             ((uVar2 = FUN_ffff0000907c846c(param_2[uVar7 - 1],param_2[uVar7 - 1],&local_a8),
              uVar2 != 0 || (uVar2 = FUN_ffff0000907cc6c4(param_2[uVar7 - 1],param_1), uVar2 != 0)))
             ))) || ((uVar2 = FUN_ffff0000907c846c
                                        (param_2[uVar7 - 1] + 0x18,param_2[uVar7 - 1] + 0x18,
                                         &local_a8), uVar2 != 0 ||
                     ((((uVar2 = FUN_ffff0000907cc6c4(param_2[uVar7 - 1] + 0x18,param_1), uVar2 != 0
                        || (uVar2 = FUN_ffff0000907c846c
                                              (param_2[uVar7 - 1] + 0x18,param_2[uVar7 - 1] + 0x18,
                                               &local_90), uVar2 != 0)) ||
                       (uVar2 = FUN_ffff0000907cc6c4(param_2[uVar7 - 1] + 0x18,param_1), uVar2 != 0)
                       ) || (uVar2 = FUN_ffff0000907c714c
                                               (param_2[uVar7 - 1],*(undefined8 *)(param_1 + 0x10)),
                            uVar2 != 0)))))) goto LAB_ffff0000907cc658;
        uVar2 = FUN_ffff0000907c714c(param_2[uVar7 - 1] + 0x18,*(undefined8 *)(param_1 + 0x10));
        uVar3 = (ulong)uVar2;
        if (uVar2 != 0) break;
        FUN_ffff0000907c7058(param_2[uVar7 - 1] + 0x30);
        lVar6 = lVar6 + -0x18;
        uVar7 = uVar7 - 1;
      } while (uVar7 != 0);
    }
  }
LAB_ffff0000907cc65c:
  FUN_ffff0000907c7058(&local_78);
  FUN_ffff0000907c7058(&local_90);
  FUN_ffff0000907c7058(&local_a8);
  lVar6 = lVar4;
  if (param_3 < 2) {
    param_3 = 1;
  }
  do {
    FUN_ffff0000907c7058(lVar6);
    param_3 = param_3 - 1;
    lVar6 = lVar6 + 0x18;
  } while (param_3 != 0);
  thunk_FUN_ffff0000907c314c(lVar4);
  return uVar3;
LAB_ffff0000907cc658:
  uVar3 = (ulong)uVar2;
  goto LAB_ffff0000907cc65c;
}


