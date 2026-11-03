// ffff00009072a27c  size=636  FUN_ffff00009072a27c

void FUN_ffff00009072a27c(long param_1)

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
  iVar2 = FUN_ffff00009078aa68();
  lVar6 = FUN_ffff00009078acb8();
  if ((iVar2 != 0) && (lVar6 != 0)) {
    FUN_ffff00009077b06c(lVar6);
    if (param_1 == 0) {
      FUN_ffff00009077de54(s__KVM_INIT__UNMAP2__invalid_FDT_e_ffff0000907f981d);
      return;
    }
    iVar3 = FUN_ffff00009076d75c(param_1,&DAT_ffff0000907ea66c);
    if (iVar3 < 0) {
      FUN_ffff00009077de54(s__KVM_INIT__UNMAP2__invalid_FDT_r_ffff0000907f9844,iVar3);
      return;
    }
    iVar10 = 0;
    plVar11 = (long *)(lVar6 + 0x10);
    do {
      if ((*(int *)((long)plVar11 + -0xc) != 0) && ((int)plVar11[1] == 0)) {
        iVar4 = FUN_ffff00009077f434
                          (&local_78,0x13,s__s_08x_ffff0000907e0fda,s_mtkunmap2__ffff0000907f456e,
                           iVar10);
        if (iVar4 < 0xb) {
          FUN_ffff00009077de54(s__KVM_INIT__UNMAP2__failed_to_com_ffff0000907d4275,iVar10);
        }
        else {
          iVar4 = FUN_ffff00009076e878(param_1,iVar3,&local_78);
          if (iVar4 < 0) {
            pcVar7 = s__KVM_INIT__UNMAP2__failed_to_add_ffff0000907d5d74;
            iVar5 = iVar4;
          }
          else {
            iVar5 = FUN_ffff00009077ef88(s_pkvm_protected_region_ffff0000907fb429);
            iVar5 = FUN_ffff00009076e40c
                              (param_1,iVar4,s_compatible_ffff0000907d7429,
                               s_pkvm_protected_region_ffff0000907fb429,iVar5 + 1);
            if (iVar5 == 0) {
              uVar9 = plVar11[-1];
              uVar1 = (uVar9 & 0xff00ff00ff00ff00) >> 8 | (uVar9 & 0xff00ff00ff00ff) << 8;
              uVar1 = (uVar1 & 0xffff0000ffff0000) >> 0x10 | (uVar1 & 0xffff0000ffff) << 0x10;
              local_88 = uVar1 >> 0x20 | uVar1 << 0x20;
              uVar1 = (*plVar11 - uVar9 & 0xff00ff00ff00ff00) >> 8 |
                      (*plVar11 - uVar9 & 0xff00ff00ff00ff) << 8;
              uVar1 = (uVar1 & 0xffff0000ffff0000) >> 0x10 | (uVar1 & 0xffff0000ffff) << 0x10;
              local_80 = uVar1 >> 0x20 | uVar1 << 0x20;
              iVar5 = FUN_ffff00009076e40c(param_1,iVar4,&DAT_ffff0000907df5a2,&local_88,0x10);
              if (iVar5 == 0) goto LAB_ffff00009072a318;
              pcVar7 = s__KVM_INIT__UNMAP2__failed_to_ass_ffff0000907ef6a4;
            }
            else {
              pcVar7 = s__KVM_INIT__UNMAP2__failed_to_ass_ffff0000907ef64b;
            }
          }
          FUN_ffff00009077de54(pcVar7,iVar10,iVar5);
        }
        break;
      }
LAB_ffff00009072a318:
      iVar10 = iVar10 + 1;
      plVar11 = plVar11 + 4;
    } while (iVar2 != iVar10);
    local_64 = 0;
    iVar2 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_pkvm_mtkunmap2_ffff0000907fcf71);
    if (((iVar2 < 0) ||
        (piVar8 = (int *)FUN_ffff00009076dcd4
                                   (param_1,iVar2,s_clear_violations_ffff0000907f60cd,&local_64),
        piVar8 == (int *)0x0)) || (local_64 != 4)) {
      local_64 = 0;
    }
    else if (*piVar8 < 1) {
      return;
    }
    FUN_ffff00009078accc();
  }
  return;
}


