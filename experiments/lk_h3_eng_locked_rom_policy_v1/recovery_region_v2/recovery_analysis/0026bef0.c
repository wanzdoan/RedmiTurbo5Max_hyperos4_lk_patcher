
/* WARNING: Unknown calling convention -- yet parameter storage is locked */
/* CreateSnapshotPartitions() */

void CreateSnapshotPartitions(void)

{
  undefined4 uVar1;
  long lVar2;
  SnapshotManager *this;
  bool bVar3;
  int iVar4;
  ulong uVar5;
  undefined4 *puVar6;
  ostream *poVar7;
  IDeviceInfo *extraout_x1;
  IDeviceInfo *pIVar8;
  bool in_w2;
  undefined1 auVar9 [16];
  LogMessage aLStack_58 [8];
  base local_50;
  undefined7 uStack_4f;
  char cStack_48;
  char acStack_47 [5];
  undefined2 uStack_42;
  char cStack_40;
  char acStack_3f [5];
  undefined1 uStack_3a;
  undefined1 uStack_39;
  long local_38;

  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  local_50 = (base)0x2a;
  acStack_47 = (char  [5])s_ro_virtual_ab_enabled_001583fa._8_5_;
  uStack_4f = (undefined7)s_ro_virtual_ab_enabled_001583fa._0_8_;
  cStack_48 = SUB81(s_ro_virtual_ab_enabled_001583fa._0_8_,7);
  uStack_42 = (undefined2)s_ro_virtual_ab_enabled_001583fa._13_3_;
  cStack_40 = SUB31(s_ro_virtual_ab_enabled_001583fa._13_3_,2);
  acStack_3f = (char  [5])s_ro_virtual_ab_enabled_001583fa._16_5_;
  uStack_3a = 0;
  auVar9 = android::base::GetBoolProperty(&local_50,(string *)0x0,in_w2);
  pIVar8 = auVar9._8_8_;
  if (((byte)local_50 & 1) != 0) {
    operator_delete((void *)CONCAT17(uStack_39,CONCAT16(uStack_3a,CONCAT51(acStack_3f,cStack_40))));
    pIVar8 = extraout_x1;
  }
  if ((auVar9._0_8_ & 1) == 0) {
    bVar3 = true;
  }
  else {
    local_50 = (base)0x0;
    uStack_4f = 0;
    android::snapshot::SnapshotManager::New((SnapshotManager *)0x0,pIVar8);
    if ((SnapshotManager *)CONCAT71(uStack_4f,local_50) == (SnapshotManager *)0x0) {
      uVar5 = android::base::ShouldLog(3,0);
      if ((uVar5 & 1) == 0) {
        bVar3 = true;
      }
      else {
        puVar6 = (undefined4 *)__errno();
        uVar1 = *puVar6;
        android::base::LogMessage::LogMessage
                  (aLStack_58,"bootable/recovery/install/snapshot_utils.cpp",0x55,3,0,0xffffffff);
        poVar7 = (ostream *)android::base::LogMessage::stream(aLStack_58);
        std::__put_character_sequence_abi_ne180000_<char,std::char_traits<char>>
                  (poVar7,"Could not create SnapshotManager",0x20);
        android::base::LogMessage::~LogMessage(aLStack_58);
        bVar3 = true;
        *puVar6 = uVar1;
      }
    }
    else {
      iVar4 = android::snapshot::SnapshotManager::RecoveryCreateSnapshotDevices
                        ((SnapshotManager *)CONCAT71(uStack_4f,local_50));
      bVar3 = iVar4 != 0;
    }
    this = (SnapshotManager *)CONCAT71(uStack_4f,local_50);
    local_50 = (base)0x0;
    uStack_4f = 0;
    if (this != (SnapshotManager *)0x0) {
      android::snapshot::SnapshotManager::~SnapshotManager(this);
      operator_delete(this);
    }
  }
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(bVar3);
}
