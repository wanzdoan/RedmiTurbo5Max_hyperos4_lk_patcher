// ffff0000907b5aec  size=920  FUN_ffff0000907b5aec

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907b5aec(undefined8 param_1,uint param_2,long param_3,long param_4)

{
  ulong uVar1;
  long lVar2;
  undefined8 uVar3;
  int iVar4;
  ulong uVar5;
  ulong *puVar6;
  ulong uVar7;
  long *plVar8;
  uint uVar9;
  uint uVar10;
  uint uVar11;
  ulong uVar12;
  ulong uVar13;
  ulong local_a0 [6];
  undefined8 local_70;
  ulong uStack_68;
  
  local_70 = 0;
  uStack_68 = 0;
  local_a0[4] = 0;
  local_a0[5] = 0;
  local_a0[2] = 0;
  local_a0[3] = 0;
  local_a0[0] = 0;
  local_a0[1] = 0;
  if ((param_2 & 0xffffffef) != 0x20) {
    return 0x10d004;
  }
  uVar10 = param_2 >> 3;
  uVar13 = (ulong)uVar10;
  uVar11 = 0;
  uVar9 = uVar10 * 8 - 8;
  uVar12 = (ulong)uVar10;
LAB_ffff0000907b5b84:
  FUN_ffff0000907ad2dc(&local_70,s_Key_Pair_ffff0000908035ab,0x10);
  lVar2 = FUN_ffff0000907ad25c(s_Key_Pair_ffff0000908035ab);
  *(char *)((long)&local_70 + lVar2) = (char)uVar11;
  uStack_68 = uStack_68 & 0xffffffffffffff;
  uVar3 = FUN_ffff0000907ad25c(&local_70);
  uVar3 = FUN_ffff000090799010
                    (&DAT_ffff0000908133f0,0x40,&local_70,uVar3,param_1,0x20,param_3,(ulong)param_2)
  ;
  if ((int)uVar3 != 0) {
    return uVar3;
  }
  uVar5 = 0;
  uVar10 = uVar9;
  do {
    uVar7 = (ulong)uVar10;
    uVar10 = uVar10 - 8;
    uVar7 = (*(ulong *)(param_3 + uVar7) & 0xff00ff00ff00ff00) >> 8 |
            (*(ulong *)(param_3 + uVar7) & 0xff00ff00ff00ff) << 8;
    uVar7 = (uVar7 & 0xffff0000ffff0000) >> 0x10 | (uVar7 & 0xffff0000ffff) << 0x10;
    local_a0[uVar5] = uVar7 >> 0x20 | uVar7 << 0x20;
    uVar5 = uVar5 + 1;
  } while (uVar13 != uVar5);
  uVar5 = 0;
  do {
    if (local_a0[uVar5] != 0) {
      uVar5 = uVar12;
      if (param_2 != 0x20) goto LAB_ffff0000907b5c94;
      if (local_a0[3] < 0xffffffff00000000) goto LAB_ffff0000907b5d80;
      if (local_a0[3] == 0xffffffff00000000) {
        if ((local_a0[2] != -1) || (local_a0[1] < 0xbce6faada7179e84)) goto LAB_ffff0000907b5d80;
        if (local_a0[1] == 0xbce6faada7179e84) {
          if (local_a0[0] < 0xf3b9cac2fc632551) goto LAB_ffff0000907b5d80;
          if (local_a0[0] == 0xf3b9cac2fc632551) break;
        }
      }
      if (9 < uVar11) {
        uVar5 = 0;
        lVar2 = 4;
        puVar6 = local_a0;
        plVar8 = &DAT_ffff000090803f90;
        do {
          uVar7 = *plVar8 + uVar5;
          if (uVar7 != 0) {
            uVar5 = (ulong)(*puVar6 < uVar7);
          }
          lVar2 = lVar2 + -1;
          *puVar6 = *puVar6 - uVar7;
          puVar6 = puVar6 + 1;
          plVar8 = plVar8 + 1;
        } while (lVar2 != 0);
        lVar2 = 4;
        uVar10 = 0x18;
        puVar6 = local_a0;
        do {
          lVar2 = lVar2 + -1;
          uVar5 = (*puVar6 & 0xff00ff00ff00ff00) >> 8 | (*puVar6 & 0xff00ff00ff00ff) << 8;
          uVar5 = (uVar5 & 0xffff0000ffff0000) >> 0x10 | (uVar5 & 0xffff0000ffff) << 0x10;
          *(ulong *)(param_3 + (ulong)uVar10) = uVar5 >> 0x20 | uVar5 << 0x20;
          uVar10 = uVar10 - 8;
          puVar6 = puVar6 + 1;
        } while (lVar2 != 0);
        goto LAB_ffff0000907b5d80;
      }
      break;
    }
    uVar5 = uVar5 + 1;
  } while (uVar13 != uVar5);
  goto LAB_ffff0000907b5b78;
  while( true ) {
    if (local_a0[uVar5] < (ulong)(&DAT_ffff0000908133c0)[uVar5]) goto LAB_ffff0000907b5d80;
    if ((ulong)(&DAT_ffff0000908133c0)[uVar5] < local_a0[uVar5]) break;
LAB_ffff0000907b5c94:
    iVar4 = (int)uVar5;
    uVar5 = (ulong)(iVar4 - 1);
    if (iVar4 < 1) goto LAB_ffff0000907b5b78;
  }
  if (9 < uVar11) {
    uVar5 = 0;
    puVar6 = local_a0;
    uVar7 = uVar13;
    plVar8 = &DAT_ffff0000908133c0;
    do {
      uVar1 = *plVar8 + uVar5;
      if (uVar1 != 0) {
        uVar5 = (ulong)(*puVar6 < uVar1);
      }
      uVar7 = uVar7 - 1;
      *puVar6 = *puVar6 - uVar1;
      puVar6 = puVar6 + 1;
      plVar8 = plVar8 + 1;
    } while (uVar7 != 0);
    puVar6 = local_a0;
    uVar5 = uVar13;
    do {
      uVar5 = uVar5 - 1;
      uVar7 = (*puVar6 & 0xff00ff00ff00ff00) >> 8 | (*puVar6 & 0xff00ff00ff00ff) << 8;
      uVar7 = (uVar7 & 0xffff0000ffff0000) >> 0x10 | (uVar7 & 0xffff0000ffff) << 0x10;
      *(ulong *)(param_3 + (ulong)uVar9) = uVar7 >> 0x20 | uVar7 << 0x20;
      uVar9 = uVar9 - 8;
      puVar6 = puVar6 + 1;
    } while (uVar5 != 0);
LAB_ffff0000907b5d80:
    uVar5 = 0;
    _DAT_ffff00009090d8bc = _DAT_ffff00009090d8bc + uVar11;
    while (local_a0[uVar5] == 0) {
      uVar5 = uVar5 + 1;
      if (uVar13 == uVar5) {
        return 0x10d005;
      }
    }
    if (param_2 == 0x20) {
      if (0xfffffffeffffffff < local_a0[3]) {
        if (local_a0[3] != 0xffffffff00000000) {
          return 0x10d005;
        }
        if ((local_a0[2] == -1) && (0xbce6faada7179e83 < local_a0[1])) {
          if (local_a0[1] != 0xbce6faada7179e84) {
            return 0x10d005;
          }
          if (0xf3b9cac2fc632550 < local_a0[0]) {
            return 0x10d005;
          }
        }
      }
    }
    else {
      while( true ) {
        iVar4 = (int)uVar12;
        uVar12 = (ulong)(iVar4 - 1);
        if (iVar4 < 1) {
          return 0x10d005;
        }
        if (local_a0[uVar12] < (ulong)(&DAT_ffff0000908133c0)[uVar12]) break;
        if ((ulong)(&DAT_ffff0000908133c0)[uVar12] < local_a0[uVar12]) {
          return 0x10d005;
        }
      }
    }
    uVar3 = FUN_ffff0000907a2b48(param_3,param_4,param_4 + (ulong)param_2,_DAT_ffff00009090d7fc);
    return uVar3;
  }
LAB_ffff0000907b5b78:
  uVar11 = uVar11 + 1;
  if (uVar11 == 0x32) goto LAB_ffff0000907b5d80;
  goto LAB_ffff0000907b5b84;
}


