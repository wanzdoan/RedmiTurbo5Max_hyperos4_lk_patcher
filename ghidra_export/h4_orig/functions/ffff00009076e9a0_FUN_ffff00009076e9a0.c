// ffff00009076e9a0  size=416  FUN_ffff00009076e9a0

ulong FUN_ffff00009076e9a0(long param_1,undefined4 param_2,undefined8 param_3,uint param_4)

{
  undefined4 *puVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  int iVar5;
  ulong uVar6;
  uint local_34;
  
  uVar6 = FUN_ffff00009076ca9c();
  if (-1 < (int)uVar6) {
    uVar2 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
    uVar2 = uVar2 >> 0x10 | uVar2 << 0x10;
    if (uVar2 < 0x11) {
      uVar6 = 0xfffffff6;
    }
    else {
      uVar3 = (*(uint *)(param_1 + 0x24) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0x24) & 0xff00ff) << 8;
      iVar5 = FUN_ffff00009076ee30(param_1,0x10,uVar3 >> 0x10 | uVar3 << 0x10);
      if (iVar5 == 0) {
        if (0x11 < uVar2) {
          *(undefined4 *)(param_1 + 0x14) = 0x11000000;
        }
        uVar6 = FUN_ffff00009076d71c(param_1,param_2,param_3,param_4);
        if ((int)uVar6 < 0) {
          if ((int)uVar6 == -1) {
            local_34 = 0;
            iVar5 = FUN_ffff00009076cd48(param_1,param_2,&local_34);
            if (iVar5 == 1) {
              do {
                uVar2 = local_34;
                iVar5 = FUN_ffff00009076cd48(param_1,local_34,&local_34);
              } while (iVar5 - 3U < 2);
              iVar5 = (param_4 & 0xfffffffc) + 0xc;
              uVar3 = (*(uint *)(param_1 + 8) & 0xff00ff00) >> 8 |
                      (*(uint *)(param_1 + 8) & 0xff00ff) << 8;
              puVar1 = (undefined4 *)(param_1 + (int)uVar2 + (ulong)(uVar3 >> 0x10 | uVar3 << 0x10))
              ;
              uVar6 = FUN_ffff00009076f01c(param_1,puVar1,0,iVar5);
              if ((int)uVar6 == 0) {
                uVar3 = (*(uint *)(param_1 + 0x24) & 0xff00ff00) >> 8 |
                        (*(uint *)(param_1 + 0x24) & 0xff00ff) << 8;
                uVar4 = (*(uint *)(param_1 + 0xc) & 0xff00ff00) >> 8 |
                        (*(uint *)(param_1 + 0xc) & 0xff00ff) << 8;
                uVar3 = (uVar3 >> 0x10 | uVar3 << 0x10) + iVar5;
                uVar4 = (uVar4 >> 0x10 | uVar4 << 0x10) + iVar5;
                uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
                uVar4 = (uVar4 & 0xff00ff00) >> 8 | (uVar4 & 0xff00ff) << 8;
                *(uint *)(param_1 + 0x24) = uVar3 >> 0x10 | uVar3 << 0x10;
                *(uint *)(param_1 + 0xc) = uVar4 >> 0x10 | uVar4 << 0x10;
                *puVar1 = 0x1000000;
                FUN_ffff00009077ee50(puVar1 + 1,0,(long)(int)(param_4 & 0xfffffffc) + 4);
                FUN_ffff00009077eb20(puVar1 + 1,param_3,(long)(int)param_4);
                uVar6 = (ulong)uVar2;
                *(undefined4 *)((long)puVar1 + (long)iVar5 + -4) = 0x2000000;
              }
            }
            else {
              uVar6 = 0xfffffff3;
            }
          }
        }
        else {
          uVar6 = 0xfffffffe;
        }
      }
      else {
        uVar6 = 0xfffffff4;
      }
    }
  }
  return uVar6;
}


