// ffff00009074d27c  size=476  FUN_ffff00009074d27c

void FUN_ffff00009074d27c(int param_1,undefined8 param_2,int *param_3)

{
  int iVar1;
  int iVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  long *plVar6;
  long lVar7;
  long lVar8;
  int iVar9;
  uint uVar10;
  
  uVar5 = 0;
  if (param_1 != 0x2b) {
    uVar5 = (uint)(param_1 != 0x2d);
  }
  lVar7 = 1;
  if (param_1 != 0x2b) {
    lVar7 = 2;
  }
  iVar1 = param_3[5];
  plVar6 = (long *)(s_0123456789abcdef_ffff000090804a48 + (ulong)uVar5 * 8 + 0x10);
  iVar2 = *param_3;
  lVar7 = lVar7 - (ulong)uVar5;
  do {
    if (iVar2 != 0) {
      lVar8 = *plVar6;
      uVar3 = *(uint *)(lVar8 + 0x84);
      uVar5 = *(uint *)(lVar8 + 0x3c);
      uVar10 = *(uint *)(lVar8 + 0x608);
      uVar4 = *(uint *)(lVar8 + 0x60c);
      if (iVar2 == 3) {
        iVar9 = (uVar4 & 0xff) + (uVar10 >> 0x10 & 0xff) + (uVar4 >> 0x10 & 0xff);
        uVar10 = ((uVar10 >> 0x18) + (uVar4 >> 8 & 0xff) + iVar9) * iVar1 + 0x10;
        *(uint *)(lVar8 + 0x88) = uVar10;
        iVar9 = (uVar10 & 0x7fff) - iVar9 * iVar1;
        uVar10 = uVar5 & 0x7fff;
        uVar5 = *(uint *)(lVar8 + 0x8c) & 0xfff;
LAB_ffff00009074d318:
        iVar9 = iVar9 + (uVar3 & 0xfff) + uVar10 + uVar5 + 0x20;
      }
      else {
        if (iVar2 != 2) {
          if (iVar2 != 1) goto LAB_ffff00009074d330;
          iVar9 = (uVar10 >> 0x10 & 0xff) + (*(uint *)(lVar8 + 0x600) & 0xff) + (uVar4 & 0xff) +
                  (uVar4 >> 0x10 & 0xff);
          uVar10 = ((uVar10 >> 0x18) + (uVar4 >> 8 & 0xff) + iVar9) * iVar1 + 0x10;
          *(uint *)(lVar8 + 0x88) = uVar10;
          iVar9 = (uVar10 & 0x7fff) - iVar9 * iVar1;
          uVar10 = uVar3 & 0xfff;
          uVar5 = uVar5 & 0x7fff;
          uVar3 = *(uint *)(lVar8 + 0x80);
          goto LAB_ffff00009074d318;
        }
        iVar9 = (uVar10 >> 0x10 & 0xff) + (*(uint *)(lVar8 + 0x600) & 0xff) + (uVar4 & 0xff) +
                (uVar4 >> 0x10 & 0xff);
        uVar10 = ((uVar10 >> 0x18) + (uVar4 >> 8 & 0xff) + iVar9) * iVar1 + 0x10;
        *(uint *)(lVar8 + 0x88) = uVar10;
        iVar9 = ((uVar10 & 0x7fff) - iVar9 * iVar1) + (uVar3 & 0xfff) + (uVar5 & 0x7fff) + 0x1a;
      }
      *(int *)(lVar8 + 0x100) = iVar9;
    }
LAB_ffff00009074d330:
    plVar6 = plVar6 + 1;
    lVar7 = lVar7 + -1;
    if (lVar7 == 0) {
      return;
    }
  } while( true );
}


