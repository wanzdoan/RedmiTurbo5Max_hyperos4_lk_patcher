
/* android::base::LogMessage::~LogMessage() */

void __thiscall android::base::LogMessage::~LogMessage(LogMessage *this)

{
  undefined *puVar1;
  uint uVar2;
  code *pcVar3;
  long lVar4;
  int iVar5;
  char *__s;
  size_t sVar6;
  long *plVar7;
  undefined8 *puVar8;
  long lVar9;
  ulong uVar10;
  ostream *poVar11;
  undefined8 uVar12;
  ulong uVar13;
  ostream *poVar14;
  void *pvVar15;
  void *pvVar16;
  ulong local_68;
  ulong local_60;
  void *local_58;
  void *local_50;
  long local_48;

  lVar4 = tpidr_el0;
  local_48 = *(long *)(lVar4 + 0x28);
  uVar2 = *(uint *)(*(long *)this + 0x114);
  iVar5 = __isOSVersionAtLeast(0x1e,0,0);
  if (iVar5 == 0) {
    if ((int)uVar2 < DAT_0042ccf8) goto LAB_0040d824;
  }
  else {
    iVar5 = uVar2 + 2;
    if (4 < uVar2) {
      iVar5 = 7;
    }
    iVar5 = __android_log_is_loggable(iVar5,0,4);
    if (iVar5 == 0) goto LAB_0040d824;
  }
  poVar14 = *(ostream **)this;
  if (*(int *)(poVar14 + 0x120) != -1) {
    poVar14 = std::__put_character_sequence_abi_nn180000_<char,std::char_traits<char>>
                        (poVar14,": ",2);
    __s = strerror(*(int *)(*(long *)this + 0x120));
    sVar6 = strlen(__s);
    std::__put_character_sequence_abi_nn180000_<char,std::char_traits<char>>(poVar14,__s,sVar6);
    poVar14 = *(ostream **)this;
  }
  local_68 = 0;
  local_60 = 0;
  local_58 = (void *)0x0;
  if ((*(uint *)(poVar14 + 0x68) >> 4 & 1) == 0) {
    if ((*(uint *)(poVar14 + 0x68) >> 3 & 1) != 0) {
      poVar11 = poVar14 + 0x18;
      uVar13 = *(ulong *)(poVar14 + 0x28);
      goto LAB_0040d720;
    }
    uVar13 = 0;
    pvVar15 = (void *)((ulong)&local_68 | 1);
  }
  else {
    uVar10 = *(ulong *)(poVar14 + 0x38);
    uVar13 = *(ulong *)(poVar14 + 0x60);
    if (*(ulong *)(poVar14 + 0x60) < uVar10) {
      *(ulong *)(poVar14 + 0x60) = uVar10;
      uVar13 = uVar10;
    }
    poVar11 = poVar14 + 0x30;
LAB_0040d720:
    pvVar16 = *(void **)poVar11;
    uVar13 = uVar13 - (long)pvVar16;
    if (0xffffffffffffffef < uVar13) {
                    /* WARNING: Subroutine does not return */
      std::string::__throw_length_error_abi_nn180000_();
    }
    if (uVar13 < 0x17) {
      pvVar15 = (void *)((ulong)&local_68 | 1);
      local_68 = (ulong)(byte)((int)uVar13 << 1);
      if (uVar13 == 0) goto LAB_0040d77c;
    }
    else {
      pvVar15 = operator_new((uVar13 | 0xf) + 1);
      local_68 = (uVar13 | 0xf) + 2;
      local_60 = uVar13;
      local_58 = pvVar15;
    }
    memmove(pvVar15,pvVar16,uVar13);
  }
LAB_0040d77c:
  *(undefined1 *)((long)pvVar15 + uVar13) = 0;
  lVar9 = *(long *)this;
  iVar5 = *(int *)(lVar9 + 0x114);
  if (iVar5 == 6) {
    pvVar16 = (void *)((ulong)&local_68 | 1);
    if ((local_68 & 1) != 0) {
      pvVar16 = local_58;
    }
    android_set_abort_message(pvVar16);
    lVar9 = *(long *)this;
    iVar5 = *(int *)(lVar9 + 0x114);
  }
  pvVar15 = (void *)((ulong)&local_68 | 1);
  pvVar16 = pvVar15;
  if ((local_68 & 1) != 0) {
    pvVar16 = local_58;
  }
  LogLine(*(undefined8 *)(lVar9 + 0x108),*(undefined4 *)(lVar9 + 0x110),iVar5,
          *(undefined8 *)(lVar9 + 0x118),pvVar16);
  if (*(int *)(*(long *)this + 0x114) == 6) {
    iVar5 = __isOSVersionAtLeast(0x1e,0,0);
    if (iVar5 == 0) {
      if (((DAT_0057d770 & 1) == 0) && (iVar5 = __cxa_guard_acquire(&DAT_0057d770), iVar5 != 0)) {
        puVar8 = operator_new(0x30);
        puVar8[4] = puVar8;
        DAT_0057d768 = puVar8;
        *puVar8 = &PTR_SetBootMonitor_00427bc0;
        puVar8[1] = DefaultAborter;
        __cxa_guard_release(&DAT_0057d770);
      }
      plVar7 = (long *)DAT_0057d768[4];
      local_50 = pvVar15;
      if ((local_68 & 1) != 0) {
        local_50 = local_58;
      }
      if (plVar7 == (long *)0x0) {
        std::__throw_bad_function_call_abi_nn180000_();
                    /* WARNING: Does not return */
        pcVar3 = (code *)UndefinedInstructionException(0,0x40d964);
        (*pcVar3)();
      }
      (**(code **)(*plVar7 + 0x30))(plVar7,&local_50);
    }
    else {
      if ((local_68 & 1) != 0) {
        pvVar15 = local_58;
      }
      __android_log_call_aborter(pvVar15);
    }
  }
  if ((local_68 & 1) != 0) {
    operator_delete(local_58);
  }
LAB_0040d824:
  poVar14 = *(ostream **)this;
  *(undefined8 *)this = 0;
  if (poVar14 != (ostream *)0x0) {
    lVar9 = *(long *)PTR_VTT_00429b88;
    uVar12 = *(undefined8 *)(PTR_VTT_00429b88 + 0x18);
    puVar1 = PTR_vtable_00429b90 + 0x10;
    *(long *)poVar14 = lVar9;
    *(undefined8 *)(poVar14 + *(long *)(lVar9 + -0x18)) = uVar12;
    *(undefined **)(poVar14 + 8) = puVar1;
    if (((byte)poVar14[0x48] & 1) != 0) {
      operator_delete(*(void **)(poVar14 + 0x58));
    }
    std::streambuf::~streambuf((streambuf *)(poVar14 + 8));
    std::ostream::~ostream(poVar14);
    std::ios::~ios((ios *)(poVar14 + 0x70));
    operator_delete(poVar14);
  }
  if (*(long *)(lVar4 + 0x28) != local_48) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}
