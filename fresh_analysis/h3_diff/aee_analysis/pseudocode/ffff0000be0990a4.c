
long * FUN_ffff0000be0990a4(long param_1,uint param_2)

{
  ulong uVar1;
  long *plVar2;
  uint uVar3;
  ulong uVar4;
  long *plVar5;
  ulong uVar6;
  uint uVar7;
  long *plVar8;
  long lVar9;
  ulong uVar10;
  ulong uVar11;
  
  if ((param_2 & param_2 - 1) == 0) {
    uVar1 = param_1 + 0x10;
    if (uVar1 < 0x19) {
      uVar1 = 0x18;
    }
    uVar7 = param_2;
    if (param_2 < 0x11) {
      uVar7 = 0x10;
    }
    uVar3 = 0;
    if (param_2 != 0) {
      uVar3 = uVar7;
    }
    uVar11 = (ulong)uVar3;
    uVar1 = (uVar1 + 7 & 0xfffffffffffffff8) + uVar11;
    FUN_ffff0000be01a730(&DAT_ffff0000be1b7de8,0xffffffff);
    plVar5 = DAT_ffff0000be1b7e28;
    if (DAT_ffff0000be1b7e28 != &DAT_ffff0000be1b7e20) {
      do {
        plVar8 = (long *)plVar5[1];
        uVar6 = plVar5[2];
        if (uVar1 <= uVar6) goto LAB_ffff0000be099204;
        plVar5 = plVar8;
      } while (plVar8 != &DAT_ffff0000be1b7e20);
    }
    uVar6 = uVar1 + 0xfff;
    uVar10 = uVar6 & 0xfffffffffffff000;
    FUN_ffff0000be01a7c8(&DAT_ffff0000be1b7de8);
    uVar4 = FUN_ffff0000be05f38c(uVar6 >> 0xc,0xffffffff);
    if (uVar4 == 0) {
      FUN_ffff0000be05fd98
                (s__s__d__failed_to_grow_kernel_hea_ffff0000be0b182c,s_heap_grow_ffff0000be0c4b41,
                 0x235,uVar10);
    }
    else {
      *(ulong *)(uVar4 + 0x10) = uVar10;
      FUN_ffff0000be0993a0();
      if ((uVar4 < DAT_ffff0000be1b7dc8) || (DAT_ffff0000be1b7dc8 == 0)) {
        DAT_ffff0000be1b7dc8 = uVar4;
      }
      if (DAT_ffff0000be1b7dd0 + DAT_ffff0000be1b7dc8 < uVar10 + uVar4) {
        DAT_ffff0000be1b7dd0 = (uVar10 + uVar4) - DAT_ffff0000be1b7dc8;
      }
      if (-1 < (long)uVar6) {
        FUN_ffff0000be01a730(&DAT_ffff0000be1b7de8,0xffffffff);
        plVar5 = DAT_ffff0000be1b7e28;
        if (DAT_ffff0000be1b7e28 != &DAT_ffff0000be1b7e20) {
          do {
            plVar8 = (long *)plVar5[1];
            uVar6 = plVar5[2];
            if (uVar1 <= uVar6) {
LAB_ffff0000be099204:
              *plVar8 = *plVar5;
              *(long **)(*plVar5 + 8) = plVar8;
              *plVar5 = 0;
              plVar5[1] = 0;
              if (uVar1 + 0x18 < uVar6) {
                plVar2 = (long *)((long)plVar5 + uVar1);
                *plVar2 = *plVar8;
                plVar2[2] = uVar6 - uVar1;
                lVar9 = *plVar8;
                plVar5[2] = uVar1;
                plVar2[1] = (long)plVar8;
                *(long **)(lVar9 + 8) = plVar2;
                *plVar8 = (long)plVar2;
                uVar6 = uVar1;
              }
              uVar1 = DAT_ffff0000be1b7de0;
              plVar8 = plVar5 + 2;
              if (param_2 != 0) {
                plVar8 = (long *)((uVar11 - 1) + (long)(plVar5 + 2) & -uVar11);
              }
              plVar8[-2] = (long)plVar5;
              plVar8[-1] = uVar6;
              DAT_ffff0000be1b7dd8 = DAT_ffff0000be1b7dd8 - uVar6;
              if (DAT_ffff0000be1b7dd8 < uVar1) {
                DAT_ffff0000be1b7de0 = DAT_ffff0000be1b7dd8;
              }
              FUN_ffff0000be01a7c8(&DAT_ffff0000be1b7de8);
              return plVar8;
            }
            plVar5 = plVar8;
          } while (plVar8 != &DAT_ffff0000be1b7e20);
        }
        FUN_ffff0000be01a7c8(&DAT_ffff0000be1b7de8);
      }
    }
  }
  return (long *)0x0;
}


