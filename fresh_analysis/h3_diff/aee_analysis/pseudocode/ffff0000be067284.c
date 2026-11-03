
undefined4 FUN_ffff0000be067284(long param_1,uint param_2,long param_3,uint param_4)

{
  int iVar1;
  uint uVar2;
  undefined1 *puVar3;
  uint uVar4;
  int iVar5;
  long lVar6;
  undefined4 uVar7;
  ulong uVar8;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  local_70 = 0;
  uStack_68 = 0;
  uVar7 = 0x10b007;
  local_80 = 0;
  uStack_78 = 0;
  if (param_1 == 0) {
    return 0x10b002;
  }
  if (param_3 == 0) {
    return 0x10b004;
  }
  iVar1 = param_2 + 4;
  lVar6 = FUN_ffff0000be05f1bc(iVar1);
  if (lVar6 == 0) {
    return 0x10b006;
  }
  FUN_ffff0000be0607b0(lVar6,param_1,(ulong)param_2);
  uVar2 = param_4 + 0x1f;
  if (0x1f < uVar2) {
    puVar3 = (undefined1 *)(lVar6 + (ulong)param_2);
    FUN_ffff0000be060ae0(puVar3,0,4);
    iVar5 = thunk_FUN_ffff0000be07f3d4(lVar6,iVar1,&local_80);
    if (iVar5 != 0) goto LAB_ffff0000be067348;
    uVar4 = param_4;
    if (0x1f < param_4) {
      uVar4 = 0x20;
    }
    FUN_ffff0000be0607b0(param_3,&local_80,uVar4);
    if (0x3f < uVar2) {
      uVar8 = 1;
      do {
        param_4 = param_4 - 0x20;
        param_3 = param_3 + 0x20;
        FUN_ffff0000be060ae0(puVar3,0,4);
        puVar3[3] = (char)uVar8;
        if (((0xff < uVar8) && (puVar3[2] = (char)(uVar8 >> 8), 0xffff < uVar8)) &&
           (puVar3[1] = (char)(uVar8 >> 0x10), uVar8 >> 0x18 != 0)) {
          uVar7 = 0x10b001;
          *puVar3 = (char)(uVar8 >> 0x18);
          goto LAB_ffff0000be067348;
        }
        iVar5 = thunk_FUN_ffff0000be07f3d4(lVar6,iVar1,&local_80);
        if (iVar5 != 0) goto LAB_ffff0000be067348;
        uVar4 = param_4;
        if (0x1f < param_4) {
          uVar4 = 0x20;
        }
        FUN_ffff0000be0607b0(param_3,&local_80,uVar4);
        uVar8 = uVar8 + 1;
      } while (uVar2 >> 5 != uVar8);
    }
  }
  uVar7 = 0;
LAB_ffff0000be067348:
  thunk_FUN_ffff0000be09937c(lVar6);
  return uVar7;
}


