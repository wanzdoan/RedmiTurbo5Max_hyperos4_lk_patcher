// File offset entry: 0xa9f2c

int FUN_020a9e2c(long param_1,undefined4 *param_2,uint param_3)

{
  uint uVar1;
  char cVar2;
  long lVar3;
  bool bVar4;
  int iVar5;
  int iVar6;
  byte bVar8;
  long lVar7;
  uint uVar9;
  
  if (param_2 == (undefined4 *)0x0) {
    iVar5 = func_0x020aab00(param_1 + 0x54,param_3);
    bVar4 = iVar5 == 0;
    cVar2 = (char)((uint)*(undefined4 *)(param_1 + 0x54) >> 0x18);
    bVar8 = (byte)((uint)*(undefined4 *)(param_1 + 0x54) >> 0x18);
  }
  else {
    uVar9 = 0xffffff9c;
    do {
      uVar9 = uVar9 + 100;
      if (0x270 < uVar9 >> 3) break;
      func_0x020a92a4(param_2,8);
      iVar5 = FUN_020a92b0(param_2,0xffff,param_3 & 0xffff,100);
    } while (iVar5 != 0);
    bVar4 = uVar9 >> 3 < 0x271;
    cVar2 = (char)((uint)*param_2 >> 0x18);
    bVar8 = (byte)((uint)*param_2 >> 0x18);
  }
  uVar9 = *(uint *)(param_1 + 0x14);
  uVar1 = *(uint *)(param_1 + 0x18);
  if (uVar9 == 0) {
LAB_020a9f4c:
    iVar5 = 0;
    if (uVar1 == 0) goto LAB_020a9f8c;
    lVar3 = 0;
    do {
      lVar7 = lVar3;
      if (lVar7 + 8 == 0x40) {
        iVar5 = 0;
        goto LAB_020a9f8c;
      }
      lVar3 = lVar7 + 8;
    } while ((uVar1 >> (ulong)(*(uint *)(lVar7 + 0x20e42d8) & 0x1f) & 1) == 0);
    lVar7 = lVar7 + 0x20e42e0;
  }
  else {
    lVar3 = 0;
    do {
      lVar7 = lVar3;
      if (lVar7 + 8 == 0x88) goto LAB_020a9f4c;
      lVar3 = lVar7 + 8;
    } while ((uVar9 >> (ulong)(*(uint *)(lVar7 + 0x20e4258) & 0x1f) & 1) == 0);
    lVar7 = lVar7 + 0x20e4260;
  }
  iVar5 = *(int *)(lVar7 + -4);
LAB_020a9f8c:
  *(uint *)(param_1 + 8) = *(uint *)(param_1 + 8) | 0x80000;
  *(uint *)(param_1 + 8) = *(uint *)(param_1 + 8) & 0xfff7ffff;
  iVar6 = FUN_020a92b0((uint *)(param_1 + 0x14),0xffffffff,0,0x9c4);
  if (iVar6 == 0) {
    FUN_020a92b0((uint *)(param_1 + 0x18),0xf0000000,0,0x9c4);
  }
  if (iVar5 == 0) {
    if ((bVar8 >> 6 & 1) == 0) {
      if (bVar4) {
        if (-1 < cVar2) {
          iVar5 = 0x724e;
        }
      }
      else {
        iVar5 = 0x7275;
      }
    }
    else {
      iVar5 = 0x72e7;
    }
  }
  return iVar5;
}


