// ffff00009076ef60  size=188  FUN_ffff00009076ef60

undefined8 FUN_ffff00009076ef60(long param_1)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  undefined8 uVar4;
  
  uVar4 = FUN_ffff00009076ca9c();
  if (-1 < (int)uVar4) {
    uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
    uVar1 = uVar1 >> 0x10 | uVar1 << 0x10;
    if (uVar1 < 0x11) {
      uVar4 = 0xfffffff6;
    }
    else {
      uVar2 = (*(uint *)(param_1 + 0x24) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0x24) & 0xff00ff) << 8;
      iVar3 = FUN_ffff00009076ee30(param_1,0x10,uVar2 >> 0x10 | uVar2 << 0x10);
      if (iVar3 == 0) {
        if (0x11 < uVar1) {
          *(undefined4 *)(param_1 + 0x14) = 0x11000000;
        }
        iVar3 = FUN_ffff00009076d698(param_1);
        uVar1 = (*(uint *)(param_1 + 0x24) & 0xff00ff00) >> 8 |
                (*(uint *)(param_1 + 0x24) & 0xff00ff) << 8;
        uVar2 = (*(uint *)(param_1 + 0x20) & 0xff00ff00) >> 8 |
                (*(uint *)(param_1 + 0x20) & 0xff00ff) << 8;
        FUN_ffff00009076ee9c
                  (param_1,param_1,iVar3 * 0x10 + 0x10,uVar1 >> 0x10 | uVar1 << 0x10,
                   uVar2 >> 0x10 | uVar2 << 0x10);
        uVar4 = 0;
        uVar1 = (*(uint *)(param_1 + 0xc) & 0xff00ff00) >> 8 |
                (*(uint *)(param_1 + 0xc) & 0xff00ff) << 8;
        uVar2 = (*(uint *)(param_1 + 0x20) & 0xff00ff00) >> 8 |
                (*(uint *)(param_1 + 0x20) & 0xff00ff) << 8;
        uVar1 = (uVar2 >> 0x10 | uVar2 << 0x10) + (uVar1 >> 0x10 | uVar1 << 0x10);
        uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
        *(uint *)(param_1 + 4) = uVar1 >> 0x10 | uVar1 << 0x10;
      }
      else {
        uVar4 = 0xfffffff4;
      }
    }
  }
  return uVar4;
}


