// ffff00009076e8a4  size=252  FUN_ffff00009076e8a4

ulong FUN_ffff00009076e8a4(long param_1,undefined4 param_2,undefined8 param_3)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  ulong uVar4;
  long lVar5;
  uint local_34;
  
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
        local_34 = 0;
        lVar5 = FUN_ffff00009076ddb4(param_1,param_2,param_3,&local_34);
        uVar4 = (ulong)local_34;
        if (lVar5 != 0) {
          uVar1 = local_34 + 3 & 0xfffffffc;
          uVar4 = FUN_ffff00009076f01c(param_1,lVar5,uVar1 + 0xc,0);
          if ((int)uVar4 == 0) {
            iVar3 = -0xc - uVar1;
            uVar1 = (*(uint *)(param_1 + 0x24) & 0xff00ff00) >> 8 |
                    (*(uint *)(param_1 + 0x24) & 0xff00ff) << 8;
            uVar2 = (*(uint *)(param_1 + 0xc) & 0xff00ff00) >> 8 |
                    (*(uint *)(param_1 + 0xc) & 0xff00ff) << 8;
            uVar1 = (uVar1 >> 0x10 | uVar1 << 0x10) + iVar3;
            uVar2 = (uVar2 >> 0x10 | uVar2 << 0x10) + iVar3;
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


