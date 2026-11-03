
long FUN_ffff0000be0562f0
               (long param_1,undefined8 param_2,undefined8 param_3,int param_4,uint *param_5,
               undefined4 *param_6)

{
  long lVar1;
  uint uVar2;
  int iVar3;
  long lVar4;
  uint uVar5;
  ulong uVar6;
  uint local_5c;
  int local_58;
  uint local_54;
  
  uVar2 = FUN_ffff0000be0557b8();
  if (-1 < (int)uVar2) {
    local_5c = 0;
    do {
      uVar6 = (ulong)uVar2;
      iVar3 = FUN_ffff0000be055660(param_1,uVar6,&local_5c);
      if (iVar3 == 3) {
        if ((int)uVar2 < 0) goto joined_r0xffff0000be056444;
        goto LAB_ffff0000be056374;
      }
      uVar5 = local_5c;
      if (iVar3 == 9) goto LAB_ffff0000be056438;
      uVar2 = local_5c;
    } while (iVar3 == 4);
LAB_ffff0000be056360:
    uVar2 = 0xffffffff;
  }
  goto joined_r0xffff0000be056444;
LAB_ffff0000be056438:
  uVar2 = 0xfffffff5;
  if ((int)uVar5 < 0) {
    uVar2 = uVar5;
  }
  goto joined_r0xffff0000be056444;
  while( true ) {
    local_54 = 0;
    while( true ) {
      uVar6 = (ulong)uVar2;
      iVar3 = FUN_ffff0000be055660(param_1,uVar6,&local_54);
      if (iVar3 == 3) break;
      uVar5 = local_54;
      if (iVar3 == 9) goto LAB_ffff0000be056438;
      uVar2 = local_54;
      if (iVar3 != 4) goto LAB_ffff0000be056360;
    }
    if ((int)uVar2 < 0) break;
LAB_ffff0000be056374:
    iVar3 = FUN_ffff0000be055800(param_1,uVar6);
    if (iVar3 < 0) {
      if (param_5 == (uint *)0x0) {
        return 0;
      }
      uVar2 = 0xfffffff3;
      goto LAB_ffff0000be056448;
    }
    uVar2 = (*(uint *)(param_1 + 8) & 0xff00ff00) >> 8 | (*(uint *)(param_1 + 8) & 0xff00ff) << 8;
    lVar1 = param_1 + (ulong)(uVar2 >> 0x10 | uVar2 << 0x10) + uVar6;
    if (param_5 != (uint *)0x0) {
      uVar2 = (*(uint *)(lVar1 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + 4) & 0xff00ff) << 8;
      *param_5 = uVar2 >> 0x10 | uVar2 << 0x10;
    }
    local_58 = 0;
    uVar2 = (*(uint *)(lVar1 + 8) & 0xff00ff00) >> 8 | (*(uint *)(lVar1 + 8) & 0xff00ff) << 8;
    lVar4 = FUN_ffff0000be055b60(param_1,uVar2 >> 0x10 | uVar2 << 0x10,&local_58);
    if ((lVar4 != 0 && local_58 == param_4) &&
       (iVar3 = FUN_ffff0000be060778(lVar4,param_3,(long)param_4), iVar3 == 0)) {
      if (param_6 == (undefined4 *)0x0) {
        return lVar1;
      }
      *param_6 = (int)uVar6;
      return lVar1;
    }
    uVar2 = FUN_ffff0000be055800(param_1,uVar6);
    if ((int)uVar2 < 0) break;
  }
joined_r0xffff0000be056444:
  if (param_5 != (uint *)0x0) {
LAB_ffff0000be056448:
    *param_5 = uVar2;
  }
  return 0;
}


