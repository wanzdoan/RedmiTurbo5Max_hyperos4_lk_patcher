
long * FUN_ffff0000b8082280(long param_1,uint param_2)

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
    FUN_ffff0000b800ad90(&DAT_ffff0000b81b9b08,0xffffffff);
    plVar5 = DAT_ffff0000b81b9b48;
    if (DAT_ffff0000b81b9b48 != &DAT_ffff0000b81b9b40) {
      do {
        plVar8 = (long *)plVar5[1];
        uVar6 = plVar5[2];
        if (uVar1 <= uVar6) goto LAB_ffff0000b80823e0;
        plVar5 = plVar8;
      } while (plVar8 != &DAT_ffff0000b81b9b40);
    }
    uVar6 = uVar1 + 0xfff;
    uVar10 = uVar6 & 0xfffffffffffff000;
    FUN_ffff0000b800ae28(&DAT_ffff0000b81b9b08);
    uVar4 = FUN_ffff0000b804be9c(uVar6 >> 0xc,0xffffffff);
    if (uVar4 == 0) {
      FUN_ffff0000b804c33c
                (s__s__d__failed_to_grow_kernel_hea_ffff0000b8090633,s_heap_grow_ffff0000b809ffb9,
                 0x235,uVar10);
    }
    else {
      *(ulong *)(uVar4 + 0x10) = uVar10;
      FUN_ffff0000b808257c();
      if ((uVar4 < DAT_ffff0000b81b9ae8) || (DAT_ffff0000b81b9ae8 == 0)) {
        DAT_ffff0000b81b9ae8 = uVar4;
      }
      if (DAT_ffff0000b81b9af0 + DAT_ffff0000b81b9ae8 < uVar10 + uVar4) {
        DAT_ffff0000b81b9af0 = (uVar10 + uVar4) - DAT_ffff0000b81b9ae8;
      }
      if (-1 < (long)uVar6) {
        FUN_ffff0000b800ad90(&DAT_ffff0000b81b9b08,0xffffffff);
        plVar5 = DAT_ffff0000b81b9b48;
        if (DAT_ffff0000b81b9b48 != &DAT_ffff0000b81b9b40) {
          do {
            plVar8 = (long *)plVar5[1];
            uVar6 = plVar5[2];
            if (uVar1 <= uVar6) {
LAB_ffff0000b80823e0:
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
              uVar1 = DAT_ffff0000b81b9b00;
              plVar8 = plVar5 + 2;
              if (param_2 != 0) {
                plVar8 = (long *)((uVar11 - 1) + (long)(plVar5 + 2) & -uVar11);
              }
              plVar8[-2] = (long)plVar5;
              plVar8[-1] = uVar6;
              DAT_ffff0000b81b9af8 = DAT_ffff0000b81b9af8 - uVar6;
              if (DAT_ffff0000b81b9af8 < uVar1) {
                DAT_ffff0000b81b9b00 = DAT_ffff0000b81b9af8;
              }
              FUN_ffff0000b800ae28(&DAT_ffff0000b81b9b08);
              return plVar8;
            }
            plVar5 = plVar8;
          } while (plVar8 != &DAT_ffff0000b81b9b40);
        }
        FUN_ffff0000b800ae28(&DAT_ffff0000b81b9b08);
      }
    }
  }
  return (long *)0x0;
}


