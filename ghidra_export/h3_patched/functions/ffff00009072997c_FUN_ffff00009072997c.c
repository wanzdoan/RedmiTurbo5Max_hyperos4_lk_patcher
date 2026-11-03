// ffff00009072997c  size=808  FUN_ffff00009072997c

undefined4 FUN_ffff00009072997c(long param_1,long param_2,long param_3)

{
  ulong uVar1;
  bool bVar2;
  int iVar3;
  undefined4 uVar4;
  long lVar5;
  long lVar6;
  long lVar7;
  long lVar8;
  long *plVar9;
  ulong uVar10;
  long lVar11;
  ulong uVar12;
  long local_88;
  ulong local_80;
  long *local_70;
  
  lVar5 = FUN_ffff00009077f16c(param_1,0xd);
  uVar4 = 0xfffffff8;
  if ((((param_1 != 0) && (param_2 != 0)) && (param_3 != 0)) &&
     ((uVar1 = lVar5 + 1, 1 < uVar1 && (uVar1 < 0xf)))) {
    lVar6 = FUN_ffff000090721af0(s_hyp_cmdline_ffff0000907df72b);
    if (lVar6 == 0) {
      uVar4 = 0xfffffffe;
    }
    else {
      lVar6 = FUN_ffff00009077ee2c();
      if (lVar6 == 0) {
        FUN_ffff00009077de54
                  (s__KVM_INIT__CMDLINE___s__unable_t_ffff0000907fcf28,
                   s_kvm_cmdline_getval_ffff0000907f7e2f);
        uVar4 = 0xfffffffb;
      }
      else {
        iVar3 = FUN_ffff0000907175f4(&LAB_ffff000090818e30,1);
        if (iVar3 == 0) {
          lVar7 = FUN_ffff00009077f308(lVar6,&DAT_ffff0000907f97ea);
          local_88 = 0;
          local_80 = 0;
          local_70 = (long *)0x0;
          uVar12 = 0;
          do {
            if ((lVar7 != 0) && (lVar8 = FUN_ffff00009077f27c(lVar7,param_1), lVar8 == lVar7)) {
              lVar8 = lVar7 + lVar5;
              plVar9 = (long *)FUN_ffff000090729ca4(0);
              if (((((plVar9 != (long *)0x0) &&
                    (((lVar11 = *plVar9, lVar11 != 0 && (plVar9[1] != 0)) &&
                     (uVar10 = FUN_ffff00009077f16c(lVar11,3), uVar10 < 3)))) &&
                   ((iVar3 = FUN_ffff00009077f080(lVar8,lVar11,uVar10), iVar3 == 0 &&
                    (*(char *)(lVar7 + uVar10 + lVar5) == '_')))) ||
                  ((plVar9 = (long *)FUN_ffff000090729ca4(1), plVar9 != (long *)0x0 &&
                   (((lVar11 = *plVar9, lVar11 != 0 && (plVar9[1] != 0)) &&
                    ((uVar10 = FUN_ffff00009077f16c(lVar11,3), uVar10 < 3 &&
                     ((iVar3 = FUN_ffff00009077f080(lVar8,lVar11,uVar10), iVar3 == 0 &&
                      (*(char *)(lVar7 + uVar10 + lVar5) == '_')))))))))) ||
                 ((plVar9 = (long *)FUN_ffff000090729ca4(2), plVar9 != (long *)0x0 &&
                  ((((lVar11 = *plVar9, lVar11 != 0 && (plVar9[1] != 0)) &&
                    (uVar10 = FUN_ffff00009077f16c(lVar11,3), uVar10 < 3)) &&
                   ((iVar3 = FUN_ffff00009077f080(lVar8,lVar11,uVar10), iVar3 == 0 &&
                    (*(char *)(lVar7 + uVar10 + lVar5) == '_')))))))) {
                lVar7 = lVar7 + uVar1 + uVar10;
                uVar10 = FUN_ffff00009077f16c(lVar7,0x3ef);
                local_80 = 0;
                if (uVar10 < 0x3ef) {
                  local_80 = uVar10;
                }
                local_88 = 0;
                if (local_80 != 0) {
                  local_88 = lVar7;
                  local_70 = plVar9;
                }
              }
            }
            lVar7 = FUN_ffff00009077f308(0,&DAT_ffff0000907f97ea);
          } while ((lVar7 != 0) && (bVar2 = uVar12 < 0x100, uVar12 = uVar12 + 1, bVar2));
          iVar3 = FUN_ffff00009071768c(&LAB_ffff000090818e30);
          if (iVar3 == 0) {
            uVar4 = 0xfffffffe;
            if ((local_80 != 0) && (local_88 != 0)) {
              uVar4 = (*(code *)local_70[1])(local_88,local_80,param_2,param_3);
            }
          }
          else {
            FUN_ffff00009077de54
                      (s__KVM_INIT__CMDLINE___s__failed_t_ffff0000907f6079,
                       s_kvm_cmdline_getval_ffff0000907f7e2f);
            uVar4 = 0xffffffe1;
          }
        }
        else {
          FUN_ffff00009077de54
                    (s__KVM_INIT__CMDLINE___s__failed_t_ffff0000907f0fd4,
                     s_kvm_cmdline_getval_ffff0000907f7e2f);
          uVar4 = 0xffffffdf;
        }
        thunk_FUN_ffff0000907c2ad8(lVar6);
      }
    }
  }
  return uVar4;
}


