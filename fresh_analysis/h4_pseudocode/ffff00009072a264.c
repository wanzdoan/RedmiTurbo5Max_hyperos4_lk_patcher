
int FUN_ffff00009072a264(void)

{
  int iVar1;
  long lVar2;
  ulong uVar3;
  undefined1 uVar4;
  ulong local_38;
  
  lVar2 = FUN_ffff00009077d514(0x3ef);
  local_38 = 0x3ef;
  if (PTR_FUN_ffff000090839998 == (undefined *)0x0) {
    iVar1 = -0x18;
    if (lVar2 == 0) {
      return -0x18;
    }
  }
  else {
    if (lVar2 == 0) {
      FUN_ffff00009077e11c
                (s__KVM_INIT__CMDLINE__KCMD___s__fa_ffff0000907cf517,
                 s_kvm_cmdline_append_kcmd_ffff0000907eab89);
      return -5;
    }
    iVar1 = FUN_ffff000090729bc0(&DAT_ffff0000907f6252,lVar2,&local_38);
    if (iVar1 == 0) {
      if (local_38 - 0x3ef < 0xfffffffffffffc12) {
        FUN_ffff00009077e11c
                  (s__KVM_INIT__CMDLINE__KCMD___s__de_ffff0000908008e1,
                   s_kvm_cmdline_append_kcmd_ffff0000907eab89);
        iVar1 = 0;
      }
      else {
        uVar3 = 0;
        do {
          uVar4 = *(undefined1 *)(lVar2 + uVar3);
          iVar1 = FUN_ffff00009077dad0(uVar4);
          if (iVar1 != 0) {
            uVar4 = 0x20;
          }
          iVar1 = FUN_ffff00009077db84(uVar4);
          if (iVar1 == 0) {
            iVar1 = -7;
            goto LAB_ffff00009072a2f0;
          }
          *(undefined1 *)(lVar2 + uVar3) = uVar4;
          uVar3 = uVar3 + 1;
        } while (uVar3 < local_38);
        *(undefined1 *)(lVar2 + local_38) = 0;
        iVar1 = FUN_ffff000090719078(lVar2);
        if (iVar1 == 0) {
          FUN_ffff000090719040();
        }
        else {
          FUN_ffff00009077e11c
                    (s__KVM_INIT__CMDLINE__KCMD___s__fa_ffff0000907f469d,
                     s_kvm_cmdline_append_kcmd_ffff0000907eab89,iVar1);
        }
      }
    }
  }
LAB_ffff00009072a2f0:
  thunk_FUN_ffff0000907c314c(lVar2);
  return iVar1;
}


