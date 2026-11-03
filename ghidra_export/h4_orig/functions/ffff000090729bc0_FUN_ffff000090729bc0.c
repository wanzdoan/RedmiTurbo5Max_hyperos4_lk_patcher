// ffff000090729bc0  size=808  FUN_ffff000090729bc0

undefined4 FUN_ffff000090729bc0(long param_1,long param_2,long param_3)

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
  
  lVar5 = FUN_ffff00009077f434(param_1,0xd);
  uVar4 = 0xfffffff8;
  if ((((param_1 != 0) && (param_2 != 0)) && (param_3 != 0)) &&
     ((uVar1 = lVar5 + 1, 1 < uVar1 && (uVar1 < 0xf)))) {
    lVar6 = FUN_ffff000090721d30(s_hyp_cmdline_ffff0000907df7e5);
    if (lVar6 == 0) {
      uVar4 = 0xfffffffe;
    }
    else {
      lVar6 = FUN_ffff00009077f0f4();
      if (lVar6 == 0) {
        FUN_ffff00009077e11c
                  (s__KVM_INIT__CMDLINE___s__unable_t_ffff0000907fd0d0,
                   s_kvm_cmdline_getval_ffff0000907f7fcb);
        uVar4 = 0xfffffffb;
      }
      else {
        iVar3 = FUN_ffff000090717738(&LAB_ffff000090818e50,1);
        if (iVar3 == 0) {
          lVar7 = FUN_ffff00009077f5d0(lVar6,&DAT_ffff0000907f9986);
          local_88 = 0;
          local_80 = 0;
          local_70 = (long *)0x0;
          uVar12 = 0;
          do {
            if ((lVar7 != 0) && (lVar8 = FUN_ffff00009077f544(lVar7,param_1), lVar8 == lVar7)) {
              lVar8 = lVar7 + lVar5;
              plVar9 = (long *)FUN_ffff000090729ee8(0);
              if (((((plVar9 != (long *)0x0) &&
                    (((lVar11 = *plVar9, lVar11 != 0 && (plVar9[1] != 0)) &&
                     (uVar10 = FUN_ffff00009077f434(lVar11,3), uVar10 < 3)))) &&
                   ((iVar3 = FUN_ffff00009077f348(lVar8,lVar11,uVar10), iVar3 == 0 &&
                    (*(char *)(lVar7 + uVar10 + lVar5) == '_')))) ||
                  ((plVar9 = (long *)FUN_ffff000090729ee8(1), plVar9 != (long *)0x0 &&
                   (((lVar11 = *plVar9, lVar11 != 0 && (plVar9[1] != 0)) &&
                    ((uVar10 = FUN_ffff00009077f434(lVar11,3), uVar10 < 3 &&
                     ((iVar3 = FUN_ffff00009077f348(lVar8,lVar11,uVar10), iVar3 == 0 &&
                      (*(char *)(lVar7 + uVar10 + lVar5) == '_')))))))))) ||
                 ((plVar9 = (long *)FUN_ffff000090729ee8(2), plVar9 != (long *)0x0 &&
                  ((((lVar11 = *plVar9, lVar11 != 0 && (plVar9[1] != 0)) &&
                    (uVar10 = FUN_ffff00009077f434(lVar11,3), uVar10 < 3)) &&
                   ((iVar3 = FUN_ffff00009077f348(lVar8,lVar11,uVar10), iVar3 == 0 &&
                    (*(char *)(lVar7 + uVar10 + lVar5) == '_')))))))) {
                lVar7 = lVar7 + uVar1 + uVar10;
                uVar10 = FUN_ffff00009077f434(lVar7,0x3ef);
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
            lVar7 = FUN_ffff00009077f5d0(0,&DAT_ffff0000907f9986);
          } while ((lVar7 != 0) && (bVar2 = uVar12 < 0x100, uVar12 = uVar12 + 1, bVar2));
          iVar3 = FUN_ffff0000907177d0(&LAB_ffff000090818e50);
          if (iVar3 == 0) {
            uVar4 = 0xfffffffe;
            if ((local_80 != 0) && (local_88 != 0)) {
              uVar4 = (*(code *)local_70[1])(local_88,local_80,param_2,param_3);
            }
          }
          else {
            FUN_ffff00009077e11c
                      (s__KVM_INIT__CMDLINE___s__failed_t_ffff0000907f621a,
                       s_kvm_cmdline_getval_ffff0000907f7fcb);
            uVar4 = 0xffffffe1;
          }
        }
        else {
          FUN_ffff00009077e11c
                    (s__KVM_INIT__CMDLINE___s__failed_t_ffff0000907f115c,
                     s_kvm_cmdline_getval_ffff0000907f7fcb);
          uVar4 = 0xffffffdf;
        }
        thunk_FUN_ffff0000907c314c(lVar6);
      }
    }
  }
  return uVar4;
}


