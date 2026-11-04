
/* set_retry_bootloader_message(int, std::vector<std::string, std::allocator<std::string > > const&)
    */

void set_retry_bootloader_message(int param_1,vector *param_2)

{
  string *psVar1;
  string *psVar2;
  undefined4 uVar3;
  long lVar4;
  ulong uVar5;
  undefined4 *puVar6;
  ostream *poVar7;
  string *psVar8;
  undefined8 uVar9;
  undefined8 uVar10;
  LogMessage aLStack_80 [8];
  ulong local_78;
  undefined8 uStack_70;
  void *local_68;
  string *local_60;
  string *local_58;
  string *local_50;
  long local_48;

  lVar4 = tpidr_el0;
  local_48 = *(long *)(lVar4 + 0x28);
  psVar1 = *(string **)param_2;
  psVar8 = *(string **)(param_2 + 8);
  local_60 = (string *)0x0;
  local_58 = (string *)0x0;
  local_50 = (string *)0x0;
  for (; psVar1 != psVar8; psVar1 = psVar1 + 0x18) {
    uVar5 = *(ulong *)(psVar1 + 8);
    psVar2 = *(string **)(psVar1 + 0x10);
    if (((byte)*psVar1 & 1) == 0) {
      psVar2 = psVar1 + 1;
      uVar5 = (ulong)((byte)*psVar1 >> 1);
    }
    uVar5 = android::base::StartsWith(psVar2,uVar5,"--retry_count",0xd);
    psVar2 = local_58;
    if ((uVar5 & 1) == 0) {
      if (local_58 < local_50) {
        if (((byte)*psVar1 & 1) == 0) {
          uVar10 = *(undefined8 *)(psVar1 + 8);
          uVar9 = *(undefined8 *)psVar1;
          *(undefined8 *)(local_58 + 0x10) = *(undefined8 *)(psVar1 + 0x10);
          *(undefined8 *)(local_58 + 8) = uVar10;
          *(undefined8 *)local_58 = uVar9;
          local_58 = local_58 + 0x18;
        }
        else {
          std::string::__init_copy_ctor_external
                    (local_58,*(char **)(psVar1 + 0x10),*(ulong *)(psVar1 + 8));
          local_58 = psVar2 + 0x18;
        }
      }
      else {
        local_58 = std::vector<std::string,std::allocator<std::string>>::
                   __push_back_slow_path<std::string_const&>
                             ((vector<std::string,std::allocator<std::string>> *)&local_60,psVar1);
      }
    }
  }
  android::base::StringPrintf("--retry_count=%d",&local_78,(ulong)(uint)param_1);
  if (local_58 < local_50) {
    *(void **)(local_58 + 0x10) = local_68;
    *(undefined8 *)(local_58 + 8) = uStack_70;
    *(ulong *)local_58 = local_78;
    local_58 = local_58 + 0x18;
  }
  else {
    local_58 = std::vector<std::string,std::allocator<std::string>>::
               __push_back_slow_path<std::string>
                         ((vector<std::string,std::allocator<std::string>> *)&local_60,
                          (string *)&local_78);
    if ((local_78 & 1) != 0) {
      operator_delete(local_68);
    }
  }
  local_78 = 0;
  uStack_70 = 0;
  local_68 = (void *)0x0;
  uVar5 = update_bootloader_message((vector *)&local_60,(string *)&local_78);
  if (((uVar5 & 1) == 0) && (uVar5 = android::base::ShouldLog(4,0), (uVar5 & 1) != 0)) {
    puVar6 = (undefined4 *)__errno();
    uVar3 = *puVar6;
    android::base::LogMessage::LogMessage
              (aLStack_80,"bootable/recovery/recovery.cpp",0x314,4,0,0xffffffff);
    poVar7 = (ostream *)android::base::LogMessage::stream(aLStack_80);
    std::TEMPNAMEPLACEHOLDERVALUE<[abi:ne180000]<char,std::char_traits<char>,std::operator<
              (poVar7,(string *)&local_78);
    android::base::LogMessage::~LogMessage(aLStack_80);
    *puVar6 = uVar3;
  }
  if ((local_78 & 1) != 0) {
    operator_delete(local_68);
  }
  psVar1 = local_60;
  psVar8 = local_58;
  if (local_60 != (string *)0x0) {
    while (psVar2 = psVar8, psVar2 != psVar1) {
      psVar8 = psVar2 + -0x18;
      if (((byte)*psVar8 & 1) != 0) {
        operator_delete(*(void **)(psVar2 + -8));
      }
    }
    local_58 = psVar1;
    operator_delete(local_60);
  }
  if (*(long *)(lVar4 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}
