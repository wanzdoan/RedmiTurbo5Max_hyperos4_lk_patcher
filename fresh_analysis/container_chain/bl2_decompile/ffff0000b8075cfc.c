
int FUN_ffff0000b8075cfc
              (uint *param_1,uint param_2,long param_3,long param_4,long param_5,long param_6,
              undefined4 param_7)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  uint *puVar4;
  long lVar5;
  long lVar6;
  
  iVar3 = 0x7247;
  if ((((param_3 != 0) && (param_4 != 0)) && (param_5 != 0)) && (param_6 != 0)) {
    if (param_2 < 4) {
      lVar5 = (ulong)param_2 << 7;
      param_1[0x83] = 2;
      param_1[0x9b] = param_2;
      param_1[0x9c] = 0;
      puVar4 = param_1 + 0x100;
      do {
        uVar1 = *(uint *)(param_5 + 0x7c + lVar5);
        lVar5 = lVar5 + -4;
        uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
        *puVar4 = uVar1 >> 0x10 | uVar1 << 0x10;
        puVar4 = puVar4 + 1;
      } while (lVar5 != -0x80);
      lVar6 = (ulong)param_2 << 7;
      puVar4 = param_1 + 0x200;
      lVar5 = lVar6;
      do {
        uVar1 = *(uint *)(param_6 + 0x7c + lVar5);
        lVar5 = lVar5 + -4;
        uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
        *puVar4 = uVar1 >> 0x10 | uVar1 << 0x10;
        puVar4 = puVar4 + 1;
      } while (lVar5 != -0x80);
      lVar5 = lVar6;
      puVar4 = param_1 + 0x300;
      do {
        uVar1 = *(uint *)(param_4 + 0x7c + lVar5);
        lVar5 = lVar5 + -4;
        uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
        *puVar4 = uVar1 >> 0x10 | uVar1 << 0x10;
        puVar4 = puVar4 + 1;
      } while (lVar5 != -0x80);
      param_1[0x40] = 0;
      iVar2 = FUN_ffff0000b8076d94(param_1 + 0x80,1,1,param_7);
      iVar3 = 0;
      if (iVar2 != 0) {
        iVar3 = 0x7241;
      }
      if (iVar3 == 0) {
        param_1[0x81] = 1;
        puVar4 = param_1 + 0x300;
        do {
          *(uint *)(param_3 + 0x7c + lVar6) =
               (*puVar4 & 0xff00) << 8 | *puVar4 << 0x18 | *puVar4 >> 8 & 0xff00 | *puVar4 >> 0x18;
          lVar6 = lVar6 + -4;
          puVar4 = puVar4 + 1;
        } while (lVar6 != -0x80);
      }
      uVar1 = *param_1;
      *param_1 = uVar1 & 0xfffffffd;
      *param_1 = uVar1 & 0xfffffffd | 2;
    }
    else {
      iVar3 = 0x7242;
    }
  }
  return iVar3;
}


