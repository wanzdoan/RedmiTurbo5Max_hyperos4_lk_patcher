// ffff00009072a020  size=328  FUN_ffff00009072a020

int FUN_ffff00009072a020(void)

{
  int iVar1;
  long lVar2;
  ulong uVar3;
  undefined1 uVar4;
  ulong local_38;
  
  lVar2 = FUN_ffff00009077d24c(0x3ef);
  local_38 = 0x3ef;
  if (PTR_FUN_ffff000090839978 == (undefined *)0x0) {
    iVar1 = -0x18;
    if (lVar2 == 0) {
      return -0x18;
    }
  }
  else {
    if (lVar2 == 0) {
      FUN_ffff00009077de54
                (s__KVM_INIT__CMDLINE__KCMD___s__fa_ffff0000907cf4f8,
                 s_kvm_cmdline_append_kcmd_ffff0000907eaa06);
      return -5;
    }
    iVar1 = FUN_ffff00009072997c(&DAT_ffff0000907f60c8,lVar2,&local_38);
    if (iVar1 == 0) {
      if (local_38 - 0x3ef < 0xfffffffffffffc12) {
        FUN_ffff00009077de54
                  (s__KVM_INIT__CMDLINE__KCMD___s__de_ffff000090800705,
                   s_kvm_cmdline_append_kcmd_ffff0000907eaa06);
        iVar1 = 0;
      }
      else {
        uVar3 = 0;
        do {
          uVar4 = *(undefined1 *)(lVar2 + uVar3);
          iVar1 = FUN_ffff00009077d808(uVar4);
          if (iVar1 != 0) {
            uVar4 = 0x20;
          }
          iVar1 = FUN_ffff00009077d8bc(uVar4);
          if (iVar1 == 0) {
            iVar1 = -7;
            goto LAB_ffff00009072a0ac;
          }
          *(undefined1 *)(lVar2 + uVar3) = uVar4;
          uVar3 = uVar3 + 1;
        } while (uVar3 < local_38);
        *(undefined1 *)(lVar2 + local_38) = 0;
        iVar1 = FUN_ffff000090718f34(lVar2);
        if (iVar1 == 0) {
          FUN_ffff000090718efc();
        }
        else {
          FUN_ffff00009077de54
                    (s__KVM_INIT__CMDLINE__KCMD___s__fa_ffff0000907f4530,
                     s_kvm_cmdline_append_kcmd_ffff0000907eaa06,iVar1);
        }
      }
    }
  }
LAB_ffff00009072a0ac:
  thunk_FUN_ffff0000907c2ad8(lVar2);
  return iVar1;
}


