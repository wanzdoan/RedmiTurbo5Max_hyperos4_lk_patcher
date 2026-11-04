
/* Reboot(std::basic_string_view<char, std::char_traits<char> >) */

void Reboot(long *param_1,ulong param_2)

{
  long lVar1;
  undefined4 uVar2;
  undefined8 *puVar3;
  ulong uVar4;
  undefined4 *puVar5;
  ostream *poVar6;
  undefined1 uVar7;
  void *__dest;
  base local_68;
  undefined7 uStack_67;
  char cStack_60;
  undefined4 uStack_5f;
  char cStack_5b;
  undefined2 uStack_5a;
  char cStack_58;
  undefined2 uStack_57;
  undefined5 uStack_55;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined8 local_40;

  if (0xffffffffffffffef < param_2) {
                    /* WARNING: Subroutine does not return */
    std::string::__throw_length_error_abi_nn180000_();
  }
  if (param_2 < 0x17) {
    __dest = (void *)((ulong)&local_68 | 1);
    local_68 = SUB41((int)param_2 << 1,0);
    if (param_2 != 0) goto LAB_00404910;
  }
  else {
    __dest = operator_new((param_2 | 0xf) + 1);
    lVar1 = (param_2 | 0xf) + 2;
    cStack_60 = (char)param_2;
    uStack_5f = (undefined4)(param_2 >> 8);
    cStack_5b = (char)(param_2 >> 0x28);
    uStack_5a = (undefined2)(param_2 >> 0x30);
    cStack_58 = (char)__dest;
    uStack_57 = (undefined2)((ulong)__dest >> 8);
    uStack_55 = (undefined5)((ulong)__dest >> 0x18);
    local_68 = SUB81(lVar1,0);
    uStack_67 = (undefined7)((ulong)lVar1 >> 8);
LAB_00404910:
    memmove(__dest,param_1,param_2);
  }
  uVar7 = 199;
  *(undefined1 *)((long)__dest + param_2) = 0;
  puVar3 = (undefined8 *)std::string::insert((ulong)&local_68,(char *)0x0,0x14f5c7);
  uStack_48 = puVar3[1];
  local_50 = *puVar3;
  local_40 = puVar3[2];
  puVar3[1] = 0;
  puVar3[2] = 0;
  *puVar3 = 0;
  if (((byte)local_68 & 1) != 0) {
    operator_delete((void *)CONCAT53(uStack_55,CONCAT21(uStack_57,cStack_58)));
  }
  if (param_2 == 8) {
    if (*param_1 == 0x746f6f6274736166) goto LAB_00404a14;
  }
  else if ((param_2 == 10) && (*param_1 == 0x64616f6c746f6f62 && (short)param_1[1] == 0x7265))
  goto LAB_00404a14;
  local_68 = (base)0x22;
  uStack_57 = 0x74;
  uStack_5f = (undefined4)s_ro_boot_quiescent_00152079._8_8_;
  cStack_5b = SUB81(s_ro_boot_quiescent_00152079._8_8_,4);
  uStack_5a = SUB82(s_ro_boot_quiescent_00152079._8_8_,5);
  cStack_58 = SUB81(s_ro_boot_quiescent_00152079._8_8_,7);
  uStack_67 = (undefined7)s_ro_boot_quiescent_00152079._0_8_;
  cStack_60 = SUB81(s_ro_boot_quiescent_00152079._0_8_,7);
  uVar4 = android::base::GetBoolProperty(&local_68,(string *)0x0,(bool)uVar7);
  if (((byte)local_68 & 1) != 0) {
    operator_delete((void *)CONCAT53(uStack_55,CONCAT21(uStack_57,cStack_58)));
  }
  if ((uVar4 & 1) != 0) {
    std::string::append((char *)&local_50,0x152cf2);
  }
LAB_00404a14:
  local_68 = (base)0x18;
  uStack_5f = 0x6c746372;
  uStack_67 = (undefined7)s_sys_powerctl_0015100a._0_8_;
  cStack_60 = SUB81(s_sys_powerctl_0015100a._0_8_,7);
  cStack_5b = 0;
  uVar4 = android::base::SetProperty((string *)&local_68,(string *)&local_50);
  if (((byte)local_68 & 1) != 0) {
    operator_delete((void *)CONCAT53(uStack_55,CONCAT21(uStack_57,cStack_58)));
  }
  if (((uVar4 & 1) == 0) && (uVar4 = android::base::ShouldLog(6,0), (uVar4 & 1) != 0)) {
    puVar5 = (undefined4 *)__errno();
    uVar2 = *puVar5;
    android::base::LogMessage::LogMessage
              ((LogMessage *)&local_68,"bootable/recovery/otautil/sysutil.cpp",0x12d,6,0,0xffffffff)
    ;
    poVar6 = (ostream *)android::base::LogMessage::stream((LogMessage *)&local_68);
    std::__put_character_sequence_abi_nn180000_<char,std::char_traits<char>>
              (poVar6,"Reboot failed",0xd);
    android::base::LogMessage::~LogMessage((LogMessage *)&local_68);
    *puVar5 = uVar2;
  }
  do {
    pause();
  } while( true );
}
