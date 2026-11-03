
void FUN_ffff00009072a430(undefined8 param_1)

{
  int iVar1;
  
  iVar1 = FUN_ffff00009072a3f0();
  if (iVar1 != 0) {
    FUN_ffff00009077e11c(s__KVM_INIT__kvm_compat_pre_proces_ffff0000907dddaf,iVar1);
  }
  iVar1 = FUN_ffff00009072a4b0(param_1);
  if (iVar1 != 0) {
    FUN_ffff00009077e11c(s__KVM_INIT__pkvm_mkp_init__failed_ffff0000907df7f1,iVar1);
  }
  iVar1 = FUN_ffff00009072a420(param_1);
  if (iVar1 != 0) {
    FUN_ffff00009077e11c(s__KVM_INIT__kvm_compat_post_proce_ffff0000907e7876,iVar1);
    return;
  }
  return;
}


