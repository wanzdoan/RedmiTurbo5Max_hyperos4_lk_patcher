
/* WARNING: Removing unreachable block (ram,0xffff0000be097c14) */

undefined8 FUN_ffff0000be0979f4(undefined8 *param_1,undefined8 *param_2,undefined8 param_3)

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
LAB_ffff0000be097a5c:
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
    iVar5 = thunk_FUN_ffff0000be060e5c(lVar9);
    if (param_1 == (undefined8 *)0x0) {
      return 0xffffffff;
    }
    if (*(int *)*param_1 != 0x1000000) {
      return 0xffffffff;
    }
    puVar6 = (undefined8 *)*plVar1;
    if (puVar6 == (undefined8 *)0x0) goto LAB_ffff0000be097be4;
    lVar10 = (long)iVar5;
    do {
      if (((lVar9 != 0) && (*(int *)*puVar6 == 0x3000000)) &&
         (iVar5 = thunk_FUN_ffff0000be060f54(puVar6[2],lVar9,lVar10), iVar5 == 0)) {
        if (*(int *)*puVar6 == 0x1000000) {
          cVar3 = *(char *)((long)*puVar6 + lVar10 + 4);
        }
        else {
          cVar3 = *(char *)(puVar6[2] + lVar10);
        }
        if (cVar3 == '\0') goto LAB_ffff0000be097b94;
      }
      plVar11 = (long *)(*plVar11 + 8);
      puVar6 = (undefined8 *)*plVar11;
    } while (puVar6 != (undefined8 *)0x0);
    goto LAB_ffff0000be097bcc;
  }
  piVar2 = (int *)*puVar8 + 1;
  iVar5 = FUN_ffff0000be060e5c(piVar2);
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
         (iVar5 = thunk_FUN_ffff0000be060f54((int *)*puVar6 + 1,piVar2,lVar9), iVar5 == 0)) {
        if (*(int *)*puVar6 == 0x1000000) {
          cVar3 = *(char *)((long)*puVar6 + lVar9 + 4);
        }
        else {
          cVar3 = *(char *)(puVar6[2] + lVar9);
        }
        if (cVar3 == '\0') goto LAB_ffff0000be097b94;
      }
      plVar11 = (long *)(*plVar11 + 8);
      puVar6 = (undefined8 *)*plVar11;
    } while (puVar6 != (undefined8 *)0x0);
    goto LAB_ffff0000be097bcc;
  }
  goto LAB_ffff0000be097be4;
LAB_ffff0000be097b94:
  if (*plVar11 == 0) {
LAB_ffff0000be097bcc:
    if (param_1 == (undefined8 *)0x0) {
      return 0xffffffff;
    }
    if (*(int *)*param_1 != 0x1000000) {
      return 0xffffffff;
    }
LAB_ffff0000be097be4:
    uVar4 = (*(uint *)*puVar8 & 0xff00ff00) >> 8 | (*(uint *)*puVar8 & 0xff00ff) << 8;
    if ((uVar4 >> 0x10 | uVar4 << 0x10 | 2) != 3) {
      FUN_ffff0000be09664c(s_ERROR___s____ffff0000be0bcb11,s_ufdt_node_add_child_ffff0000be0c2739);
      FUN_ffff0000be09664c(s_invalid_children_tag_type_ffff0000be0bcb34);
      return 0xffffffff;
    }
    *(undefined8 **)param_1[3] = puVar8;
    param_1[3] = puVar7;
    puVar8 = puVar12;
  }
  else {
    iVar5 = FUN_ffff0000be0979f4(*plVar11,puVar8,param_3);
    FUN_ffff0000be097de0(param_3,puVar8);
    puVar8 = puVar12;
    if (iVar5 < 0) {
      return 0xffffffff;
    }
  }
  goto LAB_ffff0000be097a5c;
}


