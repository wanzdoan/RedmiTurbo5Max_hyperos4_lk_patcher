// File offset entry: 0x8aa90

int FUN_0208a990(int param_1,uint param_2,long *param_3,long *param_4)

{
  byte *pbVar1;
  int iVar2;
  int iVar3;
  uint uVar4;
  ulong uVar5;
  int iVar6;
  long *plVar7;
  ulong uVar8;
  ulong uVar9;
  long lVar10;
  ulong uVar11;
  long lVar12;
  
  if (param_1 == 0) {
    return 0;
  }
  param_2 = param_2 & 0xff;
  if (param_2 < 0xd) {
    param_2 = 0xc;
  }
  plVar7 = (long *)0x20eccb8;
  uVar5 = -1L << ((ulong)param_2 & 0x3f);
  uVar4 = (uint)(-1L << ((ulong)(param_2 - 0xc) & 0x3f));
  do {
    lVar10 = *plVar7;
    if (lVar10 == 0x20eccb0) {
      return 0;
    }
    if ((*(byte *)(lVar10 + 0x18) & 1) != 0) {
      uVar8 = *(ulong *)(lVar10 + 0x20);
      uVar9 = uVar8 + ~uVar5 & uVar5;
      if ((uVar8 <= uVar9) && (uVar9 <= (uVar8 + *(ulong *)(lVar10 + 0x28)) - 1)) {
        uVar8 = uVar9 - uVar8 >> 0xc;
        uVar9 = *(ulong *)(lVar10 + 0x28) >> 0xc;
        iVar6 = (int)uVar8;
        uVar8 = uVar8 & 0xffffffff;
        while( true ) {
          uVar11 = (ulong)(uint)((int)uVar8 + param_1);
          if (uVar9 <= uVar8 || uVar9 < uVar11) break;
          iVar2 = (1 - iVar6) + (int)uVar8;
          pbVar1 = (byte *)(*(long *)(lVar10 + 0x38) + uVar8 * 0x18 + 0x10);
          iVar3 = param_1;
          while( true ) {
            if (iVar3 == 0) {
              lVar12 = uVar8 * 0x18;
              for (uVar5 = uVar8; uVar5 < uVar11; uVar5 = uVar5 + 1) {
                plVar7 = (long *)(*(long *)(lVar10 + 0x38) + lVar12);
                func_0x0208a83c(plVar7);
                *(uint *)(plVar7 + 2) = *(uint *)(plVar7 + 2) | 1;
                *(long *)(lVar10 + 0x30) = *(long *)(lVar10 + 0x30) + -1;
                if (param_4 != (long *)0x0) {
                  *plVar7 = *param_4;
                  plVar7[1] = (long)param_4;
                  *(long **)(*param_4 + 8) = plVar7;
                  *param_4 = (long)plVar7;
                }
                lVar12 = lVar12 + 0x18;
              }
              if (param_3 == (long *)0x0) {
                return param_1;
              }
              *param_3 = *(long *)(lVar10 + 0x20) + uVar8 * 0x1000;
              return param_1;
            }
            if ((*pbVar1 & 1) != 0) break;
            pbVar1 = pbVar1 + 0x18;
            iVar2 = iVar2 + 1;
            iVar3 = iVar3 + -1;
          }
          uVar8 = (ulong)((iVar2 + ~uVar4 & uVar4) + iVar6);
        }
      }
    }
    plVar7 = (long *)(lVar10 + 8);
  } while( true );
}


