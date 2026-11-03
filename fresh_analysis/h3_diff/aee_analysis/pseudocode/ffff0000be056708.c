
ulong FUN_ffff0000be056708(long param_1,undefined4 param_2,undefined8 param_3)

{
  uint uVar1;
  int iVar2;
  undefined8 uVar3;
  long lVar4;
  ulong uVar5;
  long lVar6;
  uint uVar7;
  uint local_28;
  int local_24;
  
  local_28 = 0;
  uVar3 = FUN_ffff0000be060e5c(s_compatible_ffff0000be0a98b5);
  local_24 = 0;
  lVar4 = FUN_ffff0000be0562f0
                    (param_1,param_2,s_compatible_ffff0000be0a98b5,uVar3,&local_28,&local_24);
  uVar7 = local_28;
  if (lVar4 == 0) {
    uVar5 = (ulong)local_28;
  }
  else {
    uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
    if ((((uVar1 >> 0x10 | uVar1 << 0x10) < 0x10) && ((local_24 + 4U & 7) != 0)) &&
       (uVar1 = (*(uint *)(lVar4 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar4 + 4) & 0xff00ff) << 8,
       7 < (uVar1 >> 0x10 | uVar1 << 0x10))) {
      lVar4 = lVar4 + 0x10;
    }
    else {
      lVar4 = lVar4 + 0xc;
    }
    iVar2 = FUN_ffff0000be060e5c(param_3);
    if (iVar2 <= (int)uVar7) {
      do {
        uVar5 = FUN_ffff0000be060778(param_3,lVar4,(long)(iVar2 + 1));
        if ((int)uVar5 == 0) {
          return uVar5;
        }
        lVar6 = FUN_ffff0000be060748(lVar4,0,(long)(int)uVar7);
        if (lVar6 == 0) break;
        uVar7 = uVar7 + (int)lVar4 + ~(uint)lVar6;
        lVar4 = lVar6 + 1;
      } while (iVar2 <= (int)uVar7);
    }
    uVar5 = 1;
  }
  return uVar5;
}


