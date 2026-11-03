// ffff000090771270  size=636  FUN_ffff000090771270

/* WARNING: Removing unreachable block (ram,0xffff000090771490) */

undefined8 FUN_ffff000090771270(undefined8 *param_1,undefined8 *param_2,undefined8 param_3)

{
  long *plVar1;
  int *piVar2;
  char cVar3;
  uint uVar4;
  int iVar5;
  undefined8 *puVar6;
  undefined8 *puVar7;
  undefined8 *puVar8;
  long lVar9;
  long lVar10;
  long *plVar11;
  undefined8 *puVar12;
  
  if ((param_1 != (undefined8 *)0x0) && (*(int *)*param_1 == 0x3000000)) {
    *param_1 = *param_2;
    return 0;
  }
  plVar1 = param_1 + 2;
  puVar8 = (undefined8 *)param_2[2];
LAB_ffff0000907712d8:
  if (puVar8 == (undefined8 *)0x0) {
    param_2[2] = 0;
    return 0;
  }
  iVar5 = *(int *)*puVar8;
  puVar7 = puVar8 + 1;
  puVar12 = (undefined8 *)*puVar7;
  *puVar7 = 0;
  plVar11 = plVar1;
  if (iVar5 != 0x1000000) {
    lVar9 = puVar8[2];
    iVar5 = thunk_FUN_ffff00009077f250(lVar9);
    if (param_1 == (undefined8 *)0x0) {
      return 0xffffffff;
    }
    if (*(int *)*param_1 != 0x1000000) {
      return 0xffffffff;
    }
    puVar6 = (undefined8 *)*plVar1;
    if (puVar6 == (undefined8 *)0x0) goto LAB_ffff000090771460;
    lVar10 = (long)iVar5;
    do {
      if (((lVar9 != 0) && (*(int *)*puVar6 == 0x3000000)) &&
         (iVar5 = thunk_FUN_ffff00009077f348(puVar6[2],lVar9,lVar10), iVar5 == 0)) {
        if (*(int *)*puVar6 == 0x1000000) {
          cVar3 = *(char *)((long)*puVar6 + lVar10 + 4);
        }
        else {
          cVar3 = *(char *)(puVar6[2] + lVar10);
        }
        if (cVar3 == '\0') goto LAB_ffff000090771410;
      }
      plVar11 = (long *)(*plVar11 + 8);
      puVar6 = (undefined8 *)*plVar11;
    } while (puVar6 != (undefined8 *)0x0);
    goto LAB_ffff000090771448;
  }
  piVar2 = (int *)*puVar8 + 1;
  iVar5 = FUN_ffff00009077f250(piVar2);
  if (param_1 == (undefined8 *)0x0) {
    return 0xffffffff;
  }
  if (*(int *)*param_1 != 0x1000000) {
    return 0xffffffff;
  }
  puVar6 = (undefined8 *)*plVar1;
  if (puVar6 != (undefined8 *)0x0) {
    lVar9 = (long)iVar5;
    do {
      if ((*(int *)*puVar6 == 0x1000000) &&
         (iVar5 = thunk_FUN_ffff00009077f348((int *)*puVar6 + 1,piVar2,lVar9), iVar5 == 0)) {
        if (*(int *)*puVar6 == 0x1000000) {
          cVar3 = *(char *)((long)*puVar6 + lVar9 + 4);
        }
        else {
          cVar3 = *(char *)(puVar6[2] + lVar9);
        }
        if (cVar3 == '\0') goto LAB_ffff000090771410;
      }
      plVar11 = (long *)(*plVar11 + 8);
      puVar6 = (undefined8 *)*plVar11;
    } while (puVar6 != (undefined8 *)0x0);
    goto LAB_ffff000090771448;
  }
  goto LAB_ffff000090771460;
LAB_ffff000090771410:
  if (*plVar11 == 0) {
LAB_ffff000090771448:
    if (param_1 == (undefined8 *)0x0) {
      return 0xffffffff;
    }
    if (*(int *)*param_1 != 0x1000000) {
      return 0xffffffff;
    }
LAB_ffff000090771460:
    uVar4 = (*(uint *)*puVar8 & 0xff00ff00) >> 8 | (*(uint *)*puVar8 & 0xff00ff) << 8;
    if ((uVar4 >> 0x10 | uVar4 << 0x10 | 2) != 3) {
      FUN_ffff00009076fec8(s_ERROR___s____ffff0000907f36ba,s_ufdt_node_add_child_ffff0000907fc461);
      FUN_ffff00009076fec8(s_invalid_children_tag_type_ffff0000907f36dd);
      return 0xffffffff;
    }
    *(undefined8 **)param_1[3] = puVar8;
    param_1[3] = puVar7;
    puVar8 = puVar12;
  }
  else {
    iVar5 = FUN_ffff000090771270(*plVar11,puVar8,param_3);
    FUN_ffff00009077165c(param_3,puVar8);
    puVar8 = puVar12;
    if (iVar5 < 0) {
      return 0xffffffff;
    }
  }
  goto LAB_ffff0000907712d8;
}


