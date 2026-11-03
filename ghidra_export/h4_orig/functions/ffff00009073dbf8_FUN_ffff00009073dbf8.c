// ffff00009073dbf8  size=460  FUN_ffff00009073dbf8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009073dbf8(uint *param_1,long param_2,int *param_3)

{
  undefined4 uVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  undefined4 uVar4;
  undefined4 uVar5;
  uint uVar6;
  int iVar7;
  undefined8 uVar8;
  undefined8 uVar9;
  undefined4 *puVar10;
  undefined4 *puVar11;
  ulong uVar12;
  
  iVar7 = FUN_ffff00009073bfec();
  if (iVar7 == 0) {
    if (*param_1 != 0) {
      uVar12 = 0;
      uVar6 = _DAT_ffff00001c00d51c >> 10;
      do {
        if (((uVar6 & 0xff) >> (ulong)((uint)uVar12 & 0x1f) & 1) == 0) {
          iVar7 = FUN_ffff00009077f6fc
                            (param_2 + *param_3,2000 - (long)*param_3,
                             s__LAST_PC__CORE__d_PC___0x0__core_ffff0000907f83f5,uVar12 & 0xffffffff
                            );
LAB_ffff00009073dc98:
          *param_3 = *param_3 + iVar7;
        }
        else {
          puVar10 = *(undefined4 **)(*(long *)(param_1 + 2) + uVar12 * 8);
          puVar11 = *(undefined4 **)(*(long *)(param_1 + 4) + uVar12 * 8);
          if (puVar10 != (undefined4 *)0x0 && puVar11 != (undefined4 *)0x0) {
            *puVar10 = 9;
            uVar1 = *puVar11;
            *puVar10 = 8;
            uVar2 = *puVar11;
            if (param_1[8] == 0) {
              if (uVar12 < *param_1 - param_1[1]) {
                *puVar10 = 0x7f;
                uVar3 = *puVar11;
                *puVar10 = 0x7f;
                uVar4 = *puVar11;
                *puVar10 = 0x7f;
                uVar5 = *puVar11;
                *puVar10 = 0x7f;
                uVar8 = CONCAT44(uVar3,uVar4);
                uVar9 = CONCAT44(uVar5,*puVar11);
              }
              else {
                uVar8 = 0;
                uVar9 = 0;
              }
              iVar7 = FUN_ffff00009077f6fc
                                (param_2 + *param_3,2000 - (long)*param_3,
                                 s__LAST_PC__CORE__d_PC___0x_016llx_ffff0000907de2ac,
                                 uVar12 & 0xffffffff,CONCAT44(uVar1,uVar2),uVar8,uVar9);
            }
            else {
              iVar7 = FUN_ffff00009077f6fc
                                (param_2 + *param_3,2000 - (long)*param_3,
                                 s__LAST_PC__CORE__d_PC___0x_016llx_ffff0000907fd650,
                                 uVar12 & 0xffffffff,CONCAT44(uVar1,uVar2));
            }
            goto LAB_ffff00009073dc98;
          }
        }
        uVar12 = uVar12 + 1;
      } while (uVar12 < *param_1);
    }
    iVar7 = FUN_ffff00009077f6fc(param_2 + *param_3,2000 - (long)*param_3,&DAT_ffff0000907f836a);
  }
  else {
    iVar7 = FUN_ffff00009073bdc0(param_2 + *param_3,2000 - *param_3);
  }
  *param_3 = *param_3 + iVar7;
  return 1;
}


