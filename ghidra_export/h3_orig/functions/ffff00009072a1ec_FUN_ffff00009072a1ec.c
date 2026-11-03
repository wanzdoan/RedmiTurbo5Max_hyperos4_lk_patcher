// ffff00009072a1ec  size=128  FUN_ffff00009072a1ec

void FUN_ffff00009072a1ec(undefined8 param_1)

{
  int iVar1;
  
  iVar1 = FUN_ffff00009072a1ac();
  if (iVar1 != 0) {
    FUN_ffff00009077de54(s__KVM_INIT__kvm_compat_pre_proces_ffff0000907ddd0c,iVar1);
  }
  iVar1 = FUN_ffff00009072a26c(param_1);
  if (iVar1 != 0) {
    FUN_ffff00009077de54(s__KVM_INIT__pkvm_mkp_init__failed_ffff0000907df74e,iVar1);
  }
  iVar1 = FUN_ffff00009072a1dc(param_1);
  if (iVar1 != 0) {
    FUN_ffff00009077de54(s__KVM_INIT__kvm_compat_post_proce_ffff0000907e7732,iVar1);
    return;
  }
  return;
}


