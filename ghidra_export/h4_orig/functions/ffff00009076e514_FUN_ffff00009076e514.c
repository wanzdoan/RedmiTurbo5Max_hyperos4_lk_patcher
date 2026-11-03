// ffff00009076e514  size=448  FUN_ffff00009076e514

ulong FUN_ffff00009076e514
                (long param_1,undefined8 param_2,undefined8 param_3,uint param_4,long *param_5)

{
  long lVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  uint uVar5;
  long lVar6;
  undefined4 *puVar7;
  ulong uVar8;
  ulong uVar9;
  
  uVar3 = FUN_ffff00009076cea0();
  uVar9 = (ulong)uVar3;
  if (-1 < (int)uVar3) {
    uVar5 = (*(uint *)(param_1 + 0xc) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0xc) & 0xff00ff) << 8;
    lVar1 = param_1 + (ulong)(uVar5 >> 0x10 | uVar5 << 0x10);
    iVar4 = FUN_ffff00009077f250(param_3);
    uVar5 = (*(uint *)(param_1 + 0x20) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x20) & 0xff00ff) << 8;
    lVar6 = FUN_ffff00009076d1e0(lVar1,uVar5 >> 0x10 | uVar5 << 0x10,param_3);
    if (lVar6 == 0) {
      iVar4 = iVar4 + 1;
      uVar5 = (*(uint *)(param_1 + 0xc) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0xc) & 0xff00ff) << 8;
      uVar9 = (ulong)*(byte *)(param_1 + 0x20) << 0x18 | (ulong)*(byte *)(param_1 + 0x21) << 0x10 |
              (ulong)*(byte *)(param_1 + 0x22) << 8 | (ulong)*(byte *)(param_1 + 0x23);
      uVar5 = FUN_ffff00009076f01c
                        (param_1,param_1 + (ulong)(uVar5 >> 0x10 | uVar5 << 0x10) + uVar9,0,iVar4);
      if (uVar5 != 0) {
        return (ulong)uVar5;
      }
      uVar5 = (*(uint *)(param_1 + 0x20) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0x20) & 0xff00ff) << 8;
      uVar5 = (uVar5 >> 0x10 | uVar5 << 0x10) + iVar4;
      uVar5 = (uVar5 & 0xff00ff00) >> 8 | (uVar5 & 0xff00ff) << 8;
      *(uint *)(param_1 + 0x20) = uVar5 >> 0x10 | uVar5 << 0x10;
      FUN_ffff00009077eb20(lVar1 + uVar9,param_3,(long)iVar4);
      uVar5 = (uint)uVar9;
    }
    else {
      uVar5 = (int)lVar6 - (int)lVar1;
      uVar9 = (ulong)uVar5;
    }
    if (-1 < (int)uVar5) {
      uVar5 = (*(uint *)(param_1 + 8) & 0xff00ff00) >> 8 | (*(uint *)(param_1 + 8) & 0xff00ff) << 8;
      lVar1 = param_1 + (ulong)(uVar5 >> 0x10 | uVar5 << 0x10) + (ulong)uVar3;
      iVar4 = (param_4 + 3 & 0xfffffffc) + 0xc;
      *param_5 = lVar1;
      uVar3 = FUN_ffff00009076f01c(param_1,lVar1,0,iVar4);
      uVar8 = (ulong)uVar3;
      if (uVar3 == 0) {
        uVar2 = ((uint)uVar9 & 0xff00ff00) >> 8 | ((uint)uVar9 & 0xff00ff) << 8;
        puVar7 = (undefined4 *)*param_5;
        uVar3 = (*(uint *)(param_1 + 0x24) & 0xff00ff00) >> 8 |
                (*(uint *)(param_1 + 0x24) & 0xff00ff) << 8;
        uVar5 = (*(uint *)(param_1 + 0xc) & 0xff00ff00) >> 8 |
                (*(uint *)(param_1 + 0xc) & 0xff00ff) << 8;
        uVar3 = (uVar3 >> 0x10 | uVar3 << 0x10) + iVar4;
        uVar5 = (uVar5 >> 0x10 | uVar5 << 0x10) + iVar4;
        uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
        uVar5 = (uVar5 & 0xff00ff00) >> 8 | (uVar5 & 0xff00ff) << 8;
        *(uint *)(param_1 + 0x24) = uVar3 >> 0x10 | uVar3 << 0x10;
        *(uint *)(param_1 + 0xc) = uVar5 >> 0x10 | uVar5 << 0x10;
        uVar3 = (param_4 & 0xff00ff00) >> 8 | (param_4 & 0xff00ff) << 8;
        *puVar7 = 0x3000000;
        puVar7[1] = uVar3 >> 0x10 | uVar3 << 0x10;
        puVar7[2] = uVar2 >> 0x10 | uVar2 << 0x10;
        uVar9 = uVar8;
      }
      else {
        uVar9 = uVar8;
        if (lVar6 == 0) {
          uVar5 = FUN_ffff00009077f250(param_3);
          uVar3 = (*(uint *)(param_1 + 0x20) & 0xff00ff00) >> 8 |
                  (*(uint *)(param_1 + 0x20) & 0xff00ff) << 8;
          uVar3 = (uVar3 >> 0x10 | uVar3 << 0x10) + ~uVar5;
          uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
          *(uint *)(param_1 + 0x20) = uVar3 >> 0x10 | uVar3 << 0x10;
        }
      }
    }
  }
  return uVar9;
}


