// ffff00009076eb8c  size=224  FUN_ffff00009076eb8c

undefined8 FUN_ffff00009076eb8c(long param_1,int param_2)

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
        uVar4 = FUN_ffff00009076f68c(param_1,param_2);
        if (-1 < (int)uVar4) {
          uVar1 = (*(uint *)(param_1 + 8) & 0xff00ff00) >> 8 |
                  (*(uint *)(param_1 + 8) & 0xff00ff) << 8;
          iVar3 = (int)uVar4 - param_2;
          uVar4 = FUN_ffff00009076f01c
                            (param_1,param_1 + (ulong)(uVar1 >> 0x10 | uVar1 << 0x10) +
                                     (long)param_2,iVar3,0);
          if ((int)uVar4 == 0) {
            uVar1 = (*(uint *)(param_1 + 0x24) & 0xff00ff00) >> 8 |
                    (*(uint *)(param_1 + 0x24) & 0xff00ff) << 8;
            uVar2 = (*(uint *)(param_1 + 0xc) & 0xff00ff00) >> 8 |
                    (*(uint *)(param_1 + 0xc) & 0xff00ff) << 8;
            uVar1 = (uVar1 >> 0x10 | uVar1 << 0x10) - iVar3;
            uVar2 = (uVar2 >> 0x10 | uVar2 << 0x10) - iVar3;
            uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
            uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
            *(uint *)(param_1 + 0x24) = uVar1 >> 0x10 | uVar1 << 0x10;
            *(uint *)(param_1 + 0xc) = uVar2 >> 0x10 | uVar2 << 0x10;
          }
        }
      }
      else {
        uVar4 = 0xfffffff4;
      }
    }
  }
  return uVar4;
}


