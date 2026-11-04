
/* choose_recovery_file(Device*)::$_0::TEMPNAMEPLACEHOLDERVALUE(char const*) const */

void __thiscall choose_recovery_file(Device*)::$_0::operator()(__0 *this,char *param_1)

{
  long lVar1;
  void *pvVar2;
  int iVar3;
  size_t __n;
  ulong *puVar4;
  string *psVar5;
  undefined8 uVar6;
  vector<std::string,std::allocator<std::string>> *this_00;
  char *pcVar7;
  byte local_98 [16];
  void *local_88;
  ulong local_80;
  ulong uStack_78;
  void *local_70;
  ulong local_60;
  ulong uStack_58;
  char *local_50;
  long local_48;

  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  local_60 = 0;
  uStack_58 = 0;
  local_50 = (char *)0x0;
  __n = strlen(param_1);
  if (0xffffffffffffffef < __n) {
    if (*(long *)(lVar1 + 0x28) == local_48) {
      uVar6 = std::string::__throw_length_error_abi_ne180000_();
      if ((local_80 & 1) != 0) {
        operator_delete(local_70);
      }
      if ((local_98[0] & 1) != 0) {
        operator_delete(local_88);
      }
      if ((local_60 & 1) != 0) {
        operator_delete(local_50);
      }
      if (*(long *)(lVar1 + 0x28) == local_48) {
                    /* WARNING: Subroutine does not return */
        _Unwind_Resume(uVar6);
      }
    }
    goto LAB_0022ec60;
  }
  if (__n < 0x17) {
    pcVar7 = (char *)((ulong)&local_60 | 1);
    local_60 = CONCAT71(local_60._1_7_,(char)((int)__n << 1));
    if (__n != 0) goto LAB_0022eab4;
  }
  else {
    pcVar7 = operator_new((__n | 0xf) + 1);
    local_60 = (__n | 0xf) + 2;
    uStack_58 = __n;
    local_50 = pcVar7;
LAB_0022eab4:
    memcpy(pcVar7,param_1,__n);
  }
  pcVar7[__n] = '\0';
  if (0 < **(int **)this) {
    std::to_string(**(int **)this);
    puVar4 = (ulong *)std::string::insert((ulong)local_98,(char *)0x0,0x1482c2);
    local_70 = (void *)puVar4[2];
    uStack_78 = puVar4[1];
    local_80 = *puVar4;
    puVar4[1] = 0;
    puVar4[2] = 0;
    *puVar4 = 0;
    pvVar2 = (void *)((ulong)&local_80 | 1);
    if ((local_80 & 1) != 0) {
      pvVar2 = local_70;
    }
    std::string::append((char *)&local_60,(ulong)pvVar2);
    if ((local_80 & 1) != 0) {
      operator_delete(local_70);
    }
    if ((local_98[0] & 1) != 0) {
      operator_delete(local_88);
    }
  }
  iVar3 = ensure_path_mounted((string *)&local_60);
  if (iVar3 == 0) {
    pcVar7 = (char *)((ulong)&local_60 | 1);
    if ((local_60 & 1) != 0) {
      pcVar7 = local_50;
    }
    iVar3 = access(pcVar7,4);
    if (iVar3 == 0) {
      this_00 = *(vector<std::string,std::allocator<std::string>> **)(this + 8);
      puVar4 = *(ulong **)(this_00 + 8);
      if (puVar4 < *(ulong **)(this_00 + 0x10)) {
        psVar5 = (string *)(puVar4 + 3);
        puVar4[2] = (ulong)local_50;
        puVar4[1] = uStack_58;
        *puVar4 = local_60;
        uStack_58 = 0;
        local_50 = (char *)0x0;
        local_60 = 0;
      }
      else {
        psVar5 = std::vector<std::string,std::allocator<std::string>>::
                 __push_back_slow_path<std::string>(this_00,(string *)&local_60);
      }
      *(string **)(this_00 + 8) = psVar5;
    }
  }
  if ((local_60 & 1) != 0) {
    operator_delete(local_50);
  }
  if (*(long *)(lVar1 + 0x28) == local_48) {
    return;
  }
LAB_0022ec60:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
