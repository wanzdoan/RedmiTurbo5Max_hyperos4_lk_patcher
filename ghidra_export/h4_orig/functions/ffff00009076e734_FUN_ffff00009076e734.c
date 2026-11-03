// ffff00009076e734  size=368  FUN_ffff00009076e734

undefined8
FUN_ffff00009076e734
          (long param_1,undefined4 param_2,undefined8 param_3,undefined8 param_4,int param_5)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  undefined8 uVar5;
  long lVar6;
  long lVar7;
  int local_5c;
  long local_58;
  
  uVar5 = FUN_ffff00009076ca9c();
  if (-1 < (int)uVar5) {
    uVar2 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
    uVar2 = uVar2 >> 0x10 | uVar2 << 0x10;
    if (uVar2 < 0x11) {
      uVar5 = 0xfffffff6;
    }
    else {
      uVar3 = (*(uint *)(param_1 + 0x24) & 0xff00ff00) >> 8 |
              (*(uint *)(param_1 + 0x24) & 0xff00ff) << 8;
      iVar4 = FUN_ffff00009076ee30(param_1,0x10,uVar3 >> 0x10 | uVar3 << 0x10);
      if (iVar4 == 0) {
        if (0x11 < uVar2) {
          *(undefined4 *)(param_1 + 0x14) = 0x11000000;
        }
        local_5c = 0;
        lVar6 = FUN_ffff00009076ddb4(param_1,param_2,param_3,&local_5c);
        local_58 = lVar6;
        if (lVar6 == 0) {
          uVar5 = FUN_ffff00009076e514(param_1,param_2,param_3,param_5,&local_58);
          if ((int)uVar5 != 0) {
            return uVar5;
          }
          lVar7 = local_58 + 0xc;
        }
        else {
          uVar2 = local_5c + param_5;
          uVar3 = local_5c + 3U & 0xfffffffc;
          uVar1 = uVar2 + 3 & 0xfffffffc;
          uVar5 = FUN_ffff00009076f01c(param_1,lVar6 + 0xc,uVar3,uVar1);
          if ((int)uVar5 != 0) {
            return uVar5;
          }
          iVar4 = uVar1 - uVar3;
          uVar1 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
          uVar2 = (*(uint *)(param_1 + 0x24) & 0xff00ff00) >> 8 |
                  (*(uint *)(param_1 + 0x24) & 0xff00ff) << 8;
          uVar3 = (*(uint *)(param_1 + 0xc) & 0xff00ff00) >> 8 |
                  (*(uint *)(param_1 + 0xc) & 0xff00ff) << 8;
          uVar2 = (uVar2 >> 0x10 | uVar2 << 0x10) + iVar4;
          uVar3 = (uVar3 >> 0x10 | uVar3 << 0x10) + iVar4;
          uVar2 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
          uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
          lVar7 = lVar6 + 0xc + (long)local_5c;
          *(uint *)(param_1 + 0x24) = uVar2 >> 0x10 | uVar2 << 0x10;
          *(uint *)(param_1 + 0xc) = uVar3 >> 0x10 | uVar3 << 0x10;
          *(uint *)(lVar6 + 4) = uVar1 >> 0x10 | uVar1 << 0x10;
        }
        FUN_ffff00009077eb20(lVar7,param_4,(long)param_5);
        uVar5 = 0;
      }
      else {
        uVar5 = 0xfffffff4;
      }
    }
  }
  return uVar5;
}


