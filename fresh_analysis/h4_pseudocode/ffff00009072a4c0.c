
void FUN_ffff00009072a4c0(long param_1)

{
  ulong uVar1;
  int iVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  long lVar6;
  char *pcVar7;
  int *piVar8;
  ulong uVar9;
  int iVar10;
  long *plVar11;
  ulong local_88;
  ulong local_80;
  undefined8 local_78;
  undefined7 uStack_70;
  undefined4 local_69;
  int local_64;
  
  local_78 = 0;
  uStack_70 = 0;
  local_69 = 0;
  local_88 = 0;
  local_80 = 0;
  iVar2 = FUN_ffff00009078ad28();
  lVar6 = FUN_ffff00009078af78();
  if ((iVar2 != 0) && (lVar6 != 0)) {
    FUN_ffff00009077b334(lVar6);
    if (param_1 == 0) {
      FUN_ffff00009077e11c(s__KVM_INIT__UNMAP2__invalid_FDT_e_ffff0000907f99b9);
      return;
    }
    iVar3 = FUN_ffff00009076da24(param_1,&DAT_ffff0000907ea7ef);
    if (iVar3 < 0) {
      FUN_ffff00009077e11c(s__KVM_INIT__UNMAP2__invalid_FDT_r_ffff0000907f99e0,iVar3);
      return;
    }
    iVar10 = 0;
    plVar11 = (long *)(lVar6 + 0x10);
    do {
      if ((*(int *)((long)plVar11 + -0xc) != 0) && ((int)plVar11[1] == 0)) {
        iVar4 = FUN_ffff00009077f6fc
                          (&local_78,0x13,s__s_08x_ffff0000907e10ab,s_mtkunmap2__ffff0000907f46db,
                           iVar10);
        if (iVar4 < 0xb) {
          FUN_ffff00009077e11c(s__KVM_INIT__UNMAP2__failed_to_com_ffff0000907d42b2,iVar10);
        }
        else {
          iVar4 = FUN_ffff00009076eb40(param_1,iVar3,&local_78);
          if (iVar4 < 0) {
            pcVar7 = s__KVM_INIT__UNMAP2__failed_to_add_ffff0000907d5e0a;
            iVar5 = iVar4;
          }
          else {
            iVar5 = FUN_ffff00009077f250(s_pkvm_protected_region_ffff0000907fb5c5);
            iVar5 = FUN_ffff00009076e6d4
                              (param_1,iVar4,s_compatible_ffff0000907d74bf,
                               s_pkvm_protected_region_ffff0000907fb5c5,iVar5 + 1);
            if (iVar5 == 0) {
              uVar9 = plVar11[-1];
              uVar1 = (uVar9 & 0xff00ff00ff00ff00) >> 8 | (uVar9 & 0xff00ff00ff00ff) << 8;
              uVar1 = (uVar1 & 0xffff0000ffff0000) >> 0x10 | (uVar1 & 0xffff0000ffff) << 0x10;
              local_88 = uVar1 >> 0x20 | uVar1 << 0x20;
              uVar1 = (*plVar11 - uVar9 & 0xff00ff00ff00ff00) >> 8 |
                      (*plVar11 - uVar9 & 0xff00ff00ff00ff) << 8;
              uVar1 = (uVar1 & 0xffff0000ffff0000) >> 0x10 | (uVar1 & 0xffff0000ffff) << 0x10;
              local_80 = uVar1 >> 0x20 | uVar1 << 0x20;
              iVar5 = FUN_ffff00009076e6d4(param_1,iVar4,&DAT_ffff0000907df645,&local_88,0x10);
              if (iVar5 == 0) goto LAB_ffff00009072a55c;
              pcVar7 = s__KVM_INIT__UNMAP2__failed_to_ass_ffff0000907ef815;
            }
            else {
              pcVar7 = s__KVM_INIT__UNMAP2__failed_to_ass_ffff0000907ef7bc;
            }
          }
          FUN_ffff00009077e11c(pcVar7,iVar10,iVar5);
        }
        break;
      }
LAB_ffff00009072a55c:
      iVar10 = iVar10 + 1;
      plVar11 = plVar11 + 4;
    } while (iVar2 != iVar10);
    local_64 = 0;
    iVar2 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_pkvm_mtkunmap2_ffff0000907fd119);
    if (((iVar2 < 0) ||
        (piVar8 = (int *)FUN_ffff00009076df9c
                                   (param_1,iVar2,s_clear_violations_ffff0000907f6257,&local_64),
        piVar8 == (int *)0x0)) || (local_64 != 4)) {
      local_64 = 0;
    }
    else if (*piVar8 < 1) {
      return;
    }
    FUN_ffff00009078af8c();
  }
  return;
}


