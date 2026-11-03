
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */
/* RtcRatSwitchController::setPreferredNetworkType(RatSwitchInfo) */

void __thiscall
RtcRatSwitchController::setPreferredNetworkType(RtcRatSwitchController *this,uint *param_2)

{
  char *pcVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  undefined8 uVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  undefined8 uVar9;
  undefined8 uVar10;
  undefined8 uVar11;
  undefined8 uVar12;
  undefined8 uVar13;
  undefined8 uVar14;
  undefined8 uVar15;
  undefined8 uVar16;
  undefined8 uVar17;
  undefined8 uVar18;
  undefined8 uVar19;
  undefined8 uVar20;
  undefined8 uVar21;
  undefined8 uVar22;
  undefined8 uVar23;
  undefined8 uVar24;
  undefined8 uVar25;
  undefined8 uVar26;
  undefined8 uVar27;
  uint uVar28;
  int iVar29;
  uint uVar30;
  RfxStatusManager *pRVar31;
  ulong uVar32;
  ulong uVar33;
  long lVar34;
  char *extraout_x1;
  uint uVar35;
  undefined8 local_180;
  undefined8 uStack_178;
  uint local_170;
  undefined *local_160;
  ulong uStack_158;
  char acStack_150 [8];
  undefined8 uStack_148;
  char local_140 [8];
  char acStack_138 [8];
  char acStack_130 [8];
  undefined8 uStack_128;
  char local_120 [8];
  char acStack_118 [8];
  char acStack_110 [8];
  undefined8 uStack_108;
  char local_100 [8];
  char acStack_f8 [8];
  undefined8 local_f0;
  long *local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 uStack_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined4 uStack_98;
  undefined4 local_94;
  undefined4 uStack_90;
  undefined8 uStack_8c;
  long local_78;
  
  lVar3 = tpidr_el0;
  local_78 = *(long *)(lVar3 + 0x28);
  pcVar1 = "false";
  if ((char)param_2[2] != '\0') {
    pcVar1 = "true";
  }
  RfxController::logV((char *)this,"RtcRatSwCtrl",
                      "[setPreferredNetworkType] ratSwtichInfo.card_type :%d, ratSwtichInfo.card_state: %d, ratSwtichInfo.rat_mode: %d, ratSwtichInfo.isCt3GDualMode: %s, ratSwtichInfo.ct3gStatus : %d. "
                      ,(ulong)*param_2,(ulong)param_2[1],(ulong)param_2[3],pcVar1,(ulong)param_2[4])
  ;
  uStack_178 = *(undefined8 *)(param_2 + 2);
  local_180 = *(undefined8 *)param_2;
  local_170 = param_2[4];
  uVar28 = calculateDefaultNetworkType(this,&local_180);
  if ((param_2[1] == 2) && (uVar28 == *(uint *)(this + 0xf8))) {
    RfxController::logD((char *)this,"RtcRatSwCtrl",
                        "Card state no changed! setPreferredNetworkType not executed!");
    if (*(long *)(this + 0x48) != 0) {
      local_e0 = (long *)0x0;
      local_160 = PTR_vtable_005d2be8 + 0x10;
      uStack_158 = (ulong)*(uint *)(this + 0x40);
      RfxSlotList::assignWithList((RfxSlotList *)&local_e0,(RfxSlotList *)(this + 0x48));
      RfxBaseSignal::emitSlots
                ((RfxSlotList *)&local_e0,RfxSignal2<int,int>::emitEntry,&local_160,(RfxObject *)0x0
                );
      RfxSlotList::clear((RfxSlotList *)&local_e0);
    }
    goto LAB_003ab0d0;
  }
  if ((*param_2 == 0) && ((param_2[1] != 3 && (param_2[3] == 0xffffffff)))) {
    pRVar31 = (RfxStatusManager *)
              RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
    uVar32 = RfxStatusManager::getIntValue(pRVar31,0x2d,0);
    uVar26 = s_persist_vendor_radio_raf4_002586fa._14_8_;
    uVar24 = s_persist_vendor_radio_raf4_002586fa._6_8_;
    uVar23 = _UNK_002586f8;
    uVar21 = s_persist_vendor_radio_raf3_002586dc._20_8_;
    uVar19 = s_persist_vendor_radio_raf3_002586dc._12_8_;
    uVar17 = s_persist_vendor_radio_raf3_002586dc._4_8_;
    uVar16 = _UNK_002586d8;
    uVar14 = s_persist_vendor_radio_raf2_002586be._18_8_;
    uVar12 = s_persist_vendor_radio_raf2_002586be._10_8_;
    uVar10 = s_persist_vendor_radio_raf2_002586be._2_8_;
    uVar9 = ram0x002586b8;
    uVar7 = s_persist_vendor_radio_raf1_002586a0._16_8_;
    uVar5 = s_persist_vendor_radio_raf1_002586a0._8_8_;
    uVar4 = s_persist_vendor_radio_raf1_002586a0._0_8_;
    local_f0 = 0x346661;
    acStack_118[0] = s_persist_vendor_radio_raf3_002586dc[0xc];
    acStack_118[1] = s_persist_vendor_radio_raf3_002586dc[0xd];
    acStack_118[2] = s_persist_vendor_radio_raf3_002586dc[0xe];
    acStack_118[3] = s_persist_vendor_radio_raf3_002586dc[0xf];
    acStack_118[4] = s_persist_vendor_radio_raf3_002586dc[0x10];
    acStack_118[5] = s_persist_vendor_radio_raf3_002586dc[0x11];
    acStack_118[6] = s_persist_vendor_radio_raf3_002586dc[0x12];
    acStack_118[7] = s_persist_vendor_radio_raf3_002586dc[0x13];
    local_120[0] = s_persist_vendor_radio_raf3_002586dc[4];
    local_120[1] = s_persist_vendor_radio_raf3_002586dc[5];
    local_120[2] = s_persist_vendor_radio_raf3_002586dc[6];
    local_120[3] = s_persist_vendor_radio_raf3_002586dc[7];
    local_120[4] = s_persist_vendor_radio_raf3_002586dc[8];
    local_120[5] = s_persist_vendor_radio_raf3_002586dc[9];
    local_120[6] = s_persist_vendor_radio_raf3_002586dc[10];
    local_120[7] = s_persist_vendor_radio_raf3_002586dc[0xb];
    uStack_108 = _UNK_002586f8;
    acStack_110[0] = s_persist_vendor_radio_raf3_002586dc[0x14];
    acStack_110[1] = s_persist_vendor_radio_raf3_002586dc[0x15];
    acStack_110[2] = s_persist_vendor_radio_raf3_002586dc[0x16];
    acStack_110[3] = s_persist_vendor_radio_raf3_002586dc[0x17];
    acStack_110[4] = s_persist_vendor_radio_raf3_002586dc[0x18];
    acStack_110[5] = s_persist_vendor_radio_raf3_002586dc[0x19];
    acStack_110[6] = s_persist_vendor_radio_raf3_002586dc[0x1a];
    acStack_110[7] = s_persist_vendor_radio_raf3_002586dc[0x1b];
    acStack_f8[0] = s_persist_vendor_radio_raf4_002586fa[0xe];
    acStack_f8[1] = s_persist_vendor_radio_raf4_002586fa[0xf];
    acStack_f8[2] = s_persist_vendor_radio_raf4_002586fa[0x10];
    acStack_f8[3] = s_persist_vendor_radio_raf4_002586fa[0x11];
    acStack_f8[4] = s_persist_vendor_radio_raf4_002586fa[0x12];
    acStack_f8[5] = s_persist_vendor_radio_raf4_002586fa[0x13];
    acStack_f8[6] = s_persist_vendor_radio_raf4_002586fa[0x14];
    acStack_f8[7] = s_persist_vendor_radio_raf4_002586fa[0x15];
    local_100[0] = s_persist_vendor_radio_raf4_002586fa[6];
    local_100[1] = s_persist_vendor_radio_raf4_002586fa[7];
    local_100[2] = s_persist_vendor_radio_raf4_002586fa[8];
    local_100[3] = s_persist_vendor_radio_raf4_002586fa[9];
    local_100[4] = s_persist_vendor_radio_raf4_002586fa[10];
    local_100[5] = s_persist_vendor_radio_raf4_002586fa[0xb];
    local_100[6] = s_persist_vendor_radio_raf4_002586fa[0xc];
    local_100[7] = s_persist_vendor_radio_raf4_002586fa[0xd];
    uStack_158._0_1_ = s_persist_vendor_radio_raf1_002586a0[8];
    uStack_158._1_1_ = s_persist_vendor_radio_raf1_002586a0[9];
    uStack_158._2_1_ = s_persist_vendor_radio_raf1_002586a0[10];
    uStack_158._3_1_ = s_persist_vendor_radio_raf1_002586a0[0xb];
    uStack_158._4_1_ = s_persist_vendor_radio_raf1_002586a0[0xc];
    uStack_158._5_1_ = s_persist_vendor_radio_raf1_002586a0[0xd];
    uStack_158._6_1_ = s_persist_vendor_radio_raf1_002586a0[0xe];
    uStack_158._7_1_ = s_persist_vendor_radio_raf1_002586a0[0xf];
    local_160 = (undefined *)s_persist_vendor_radio_raf1_002586a0._0_8_;
    uStack_148 = ram0x002586b8;
    acStack_150[0] = s_persist_vendor_radio_raf1_002586a0[0x10];
    acStack_150[1] = s_persist_vendor_radio_raf1_002586a0[0x11];
    acStack_150[2] = s_persist_vendor_radio_raf1_002586a0[0x12];
    acStack_150[3] = s_persist_vendor_radio_raf1_002586a0[0x13];
    acStack_150[4] = s_persist_vendor_radio_raf1_002586a0[0x14];
    acStack_150[5] = s_persist_vendor_radio_raf1_002586a0[0x15];
    acStack_150[6] = s_persist_vendor_radio_raf1_002586a0[0x16];
    acStack_150[7] = s_persist_vendor_radio_raf1_002586a0[0x17];
    uStack_8c = 0;
    uStack_90 = 0;
    uStack_c8 = 0;
    local_d0 = 0;
    uStack_b8 = 0;
    uStack_c0 = 0;
    uStack_a8 = 0;
    local_b0 = 0;
    uStack_98 = 0;
    local_94 = 0;
    uStack_a0 = 0;
    acStack_138[0] = s_persist_vendor_radio_raf2_002586be[10];
    acStack_138[1] = s_persist_vendor_radio_raf2_002586be[0xb];
    acStack_138[2] = s_persist_vendor_radio_raf2_002586be[0xc];
    acStack_138[3] = s_persist_vendor_radio_raf2_002586be[0xd];
    acStack_138[4] = s_persist_vendor_radio_raf2_002586be[0xe];
    acStack_138[5] = s_persist_vendor_radio_raf2_002586be[0xf];
    acStack_138[6] = s_persist_vendor_radio_raf2_002586be[0x10];
    acStack_138[7] = s_persist_vendor_radio_raf2_002586be[0x11];
    local_140[0] = s_persist_vendor_radio_raf2_002586be[2];
    local_140[1] = s_persist_vendor_radio_raf2_002586be[3];
    local_140[2] = s_persist_vendor_radio_raf2_002586be[4];
    local_140[3] = s_persist_vendor_radio_raf2_002586be[5];
    local_140[4] = s_persist_vendor_radio_raf2_002586be[6];
    local_140[5] = s_persist_vendor_radio_raf2_002586be[7];
    local_140[6] = s_persist_vendor_radio_raf2_002586be[8];
    local_140[7] = s_persist_vendor_radio_raf2_002586be[9];
    uStack_128 = _UNK_002586d8;
    acStack_130[0] = s_persist_vendor_radio_raf2_002586be[0x12];
    acStack_130[1] = s_persist_vendor_radio_raf2_002586be[0x13];
    acStack_130[2] = s_persist_vendor_radio_raf2_002586be[0x14];
    acStack_130[3] = s_persist_vendor_radio_raf2_002586be[0x15];
    acStack_130[4] = s_persist_vendor_radio_raf2_002586be[0x16];
    acStack_130[5] = s_persist_vendor_radio_raf2_002586be[0x17];
    acStack_130[6] = s_persist_vendor_radio_raf2_002586be[0x18];
    acStack_130[7] = s_persist_vendor_radio_raf2_002586be[0x19];
    uStack_d8 = 0;
    local_e0 = (long *)0x0;
    rfx_property_get((long)&local_160 + (long)*(int *)(this + 0x40) * 0x1e,&local_e0,&DAT_001f9509);
    uVar33 = strtol((char *)&local_e0,(char **)0x0,10);
    RfxController::logV((char *)this,"RtcRatSwCtrl","slotCapability:%d PreviousRaf:%d ",
                        uVar32 & 0xffffffff,uVar33 & 0xffffffff);
    uVar27 = s_persist_vendor_radio_raf4_002586fa._14_8_;
    uVar25 = s_persist_vendor_radio_raf4_002586fa._6_8_;
    uVar22 = s_persist_vendor_radio_raf3_002586dc._20_8_;
    uVar20 = s_persist_vendor_radio_raf3_002586dc._12_8_;
    uVar18 = s_persist_vendor_radio_raf3_002586dc._4_8_;
    uVar15 = s_persist_vendor_radio_raf2_002586be._18_8_;
    uVar13 = s_persist_vendor_radio_raf2_002586be._10_8_;
    uVar11 = s_persist_vendor_radio_raf2_002586be._2_8_;
    uVar8 = s_persist_vendor_radio_raf1_002586a0._16_8_;
    uVar6 = s_persist_vendor_radio_raf1_002586a0._8_8_;
    local_f0 = 0x346661;
    s_persist_vendor_radio_raf3_002586dc[0xc] = (char)uVar19;
    s_persist_vendor_radio_raf3_002586dc[0xd] = SUB81(uVar19,1);
    s_persist_vendor_radio_raf3_002586dc[0xe] = SUB81(uVar19,2);
    s_persist_vendor_radio_raf3_002586dc[0xf] = SUB81(uVar19,3);
    s_persist_vendor_radio_raf3_002586dc[0x10] = SUB81(uVar19,4);
    s_persist_vendor_radio_raf3_002586dc[0x11] = SUB81(uVar19,5);
    s_persist_vendor_radio_raf3_002586dc[0x12] = SUB81(uVar19,6);
    s_persist_vendor_radio_raf3_002586dc[0x13] = SUB81(uVar19,7);
    acStack_118[0] = s_persist_vendor_radio_raf3_002586dc[0xc];
    acStack_118[1] = s_persist_vendor_radio_raf3_002586dc[0xd];
    acStack_118[2] = s_persist_vendor_radio_raf3_002586dc[0xe];
    acStack_118[3] = s_persist_vendor_radio_raf3_002586dc[0xf];
    acStack_118[4] = s_persist_vendor_radio_raf3_002586dc[0x10];
    acStack_118[5] = s_persist_vendor_radio_raf3_002586dc[0x11];
    acStack_118[6] = s_persist_vendor_radio_raf3_002586dc[0x12];
    acStack_118[7] = s_persist_vendor_radio_raf3_002586dc[0x13];
    s_persist_vendor_radio_raf3_002586dc[4] = (char)uVar17;
    s_persist_vendor_radio_raf3_002586dc[5] = SUB81(uVar17,1);
    s_persist_vendor_radio_raf3_002586dc[6] = SUB81(uVar17,2);
    s_persist_vendor_radio_raf3_002586dc[7] = SUB81(uVar17,3);
    s_persist_vendor_radio_raf3_002586dc[8] = SUB81(uVar17,4);
    s_persist_vendor_radio_raf3_002586dc[9] = SUB81(uVar17,5);
    s_persist_vendor_radio_raf3_002586dc[10] = SUB81(uVar17,6);
    s_persist_vendor_radio_raf3_002586dc[0xb] = SUB81(uVar17,7);
    local_120[0] = s_persist_vendor_radio_raf3_002586dc[4];
    local_120[1] = s_persist_vendor_radio_raf3_002586dc[5];
    local_120[2] = s_persist_vendor_radio_raf3_002586dc[6];
    local_120[3] = s_persist_vendor_radio_raf3_002586dc[7];
    local_120[4] = s_persist_vendor_radio_raf3_002586dc[8];
    local_120[5] = s_persist_vendor_radio_raf3_002586dc[9];
    local_120[6] = s_persist_vendor_radio_raf3_002586dc[10];
    local_120[7] = s_persist_vendor_radio_raf3_002586dc[0xb];
    uStack_108 = uVar23;
    s_persist_vendor_radio_raf3_002586dc[0x14] = (char)uVar21;
    s_persist_vendor_radio_raf3_002586dc[0x15] = SUB81(uVar21,1);
    s_persist_vendor_radio_raf3_002586dc[0x16] = SUB81(uVar21,2);
    s_persist_vendor_radio_raf3_002586dc[0x17] = SUB81(uVar21,3);
    s_persist_vendor_radio_raf3_002586dc[0x18] = SUB81(uVar21,4);
    s_persist_vendor_radio_raf3_002586dc[0x19] = SUB81(uVar21,5);
    s_persist_vendor_radio_raf3_002586dc[0x1a] = SUB81(uVar21,6);
    s_persist_vendor_radio_raf3_002586dc[0x1b] = SUB81(uVar21,7);
    acStack_110[0] = s_persist_vendor_radio_raf3_002586dc[0x14];
    acStack_110[1] = s_persist_vendor_radio_raf3_002586dc[0x15];
    acStack_110[2] = s_persist_vendor_radio_raf3_002586dc[0x16];
    acStack_110[3] = s_persist_vendor_radio_raf3_002586dc[0x17];
    acStack_110[4] = s_persist_vendor_radio_raf3_002586dc[0x18];
    acStack_110[5] = s_persist_vendor_radio_raf3_002586dc[0x19];
    acStack_110[6] = s_persist_vendor_radio_raf3_002586dc[0x1a];
    acStack_110[7] = s_persist_vendor_radio_raf3_002586dc[0x1b];
    s_persist_vendor_radio_raf4_002586fa[0xe] = (char)uVar26;
    s_persist_vendor_radio_raf4_002586fa[0xf] = SUB81(uVar26,1);
    s_persist_vendor_radio_raf4_002586fa[0x10] = SUB81(uVar26,2);
    s_persist_vendor_radio_raf4_002586fa[0x11] = SUB81(uVar26,3);
    s_persist_vendor_radio_raf4_002586fa[0x12] = SUB81(uVar26,4);
    s_persist_vendor_radio_raf4_002586fa[0x13] = SUB81(uVar26,5);
    s_persist_vendor_radio_raf4_002586fa[0x14] = SUB81(uVar26,6);
    s_persist_vendor_radio_raf4_002586fa[0x15] = SUB81(uVar26,7);
    acStack_f8[0] = s_persist_vendor_radio_raf4_002586fa[0xe];
    acStack_f8[1] = s_persist_vendor_radio_raf4_002586fa[0xf];
    acStack_f8[2] = s_persist_vendor_radio_raf4_002586fa[0x10];
    acStack_f8[3] = s_persist_vendor_radio_raf4_002586fa[0x11];
    acStack_f8[4] = s_persist_vendor_radio_raf4_002586fa[0x12];
    acStack_f8[5] = s_persist_vendor_radio_raf4_002586fa[0x13];
    acStack_f8[6] = s_persist_vendor_radio_raf4_002586fa[0x14];
    acStack_f8[7] = s_persist_vendor_radio_raf4_002586fa[0x15];
    s_persist_vendor_radio_raf4_002586fa[6] = (char)uVar24;
    s_persist_vendor_radio_raf4_002586fa[7] = SUB81(uVar24,1);
    s_persist_vendor_radio_raf4_002586fa[8] = SUB81(uVar24,2);
    s_persist_vendor_radio_raf4_002586fa[9] = SUB81(uVar24,3);
    s_persist_vendor_radio_raf4_002586fa[10] = SUB81(uVar24,4);
    s_persist_vendor_radio_raf4_002586fa[0xb] = SUB81(uVar24,5);
    s_persist_vendor_radio_raf4_002586fa[0xc] = SUB81(uVar24,6);
    s_persist_vendor_radio_raf4_002586fa[0xd] = SUB81(uVar24,7);
    local_100[0] = s_persist_vendor_radio_raf4_002586fa[6];
    local_100[1] = s_persist_vendor_radio_raf4_002586fa[7];
    local_100[2] = s_persist_vendor_radio_raf4_002586fa[8];
    local_100[3] = s_persist_vendor_radio_raf4_002586fa[9];
    local_100[4] = s_persist_vendor_radio_raf4_002586fa[10];
    local_100[5] = s_persist_vendor_radio_raf4_002586fa[0xb];
    local_100[6] = s_persist_vendor_radio_raf4_002586fa[0xc];
    local_100[7] = s_persist_vendor_radio_raf4_002586fa[0xd];
    s_persist_vendor_radio_raf2_002586be[10] = (char)uVar12;
    s_persist_vendor_radio_raf2_002586be[0xb] = SUB81(uVar12,1);
    s_persist_vendor_radio_raf2_002586be[0xc] = SUB81(uVar12,2);
    s_persist_vendor_radio_raf2_002586be[0xd] = SUB81(uVar12,3);
    s_persist_vendor_radio_raf2_002586be[0xe] = SUB81(uVar12,4);
    s_persist_vendor_radio_raf2_002586be[0xf] = SUB81(uVar12,5);
    s_persist_vendor_radio_raf2_002586be[0x10] = SUB81(uVar12,6);
    s_persist_vendor_radio_raf2_002586be[0x11] = SUB81(uVar12,7);
    acStack_138[0] = s_persist_vendor_radio_raf2_002586be[10];
    acStack_138[1] = s_persist_vendor_radio_raf2_002586be[0xb];
    acStack_138[2] = s_persist_vendor_radio_raf2_002586be[0xc];
    acStack_138[3] = s_persist_vendor_radio_raf2_002586be[0xd];
    acStack_138[4] = s_persist_vendor_radio_raf2_002586be[0xe];
    acStack_138[5] = s_persist_vendor_radio_raf2_002586be[0xf];
    acStack_138[6] = s_persist_vendor_radio_raf2_002586be[0x10];
    acStack_138[7] = s_persist_vendor_radio_raf2_002586be[0x11];
    s_persist_vendor_radio_raf2_002586be[2] = (char)uVar10;
    s_persist_vendor_radio_raf2_002586be[3] = SUB81(uVar10,1);
    s_persist_vendor_radio_raf2_002586be[4] = SUB81(uVar10,2);
    s_persist_vendor_radio_raf2_002586be[5] = SUB81(uVar10,3);
    s_persist_vendor_radio_raf2_002586be[6] = SUB81(uVar10,4);
    s_persist_vendor_radio_raf2_002586be[7] = SUB81(uVar10,5);
    s_persist_vendor_radio_raf2_002586be[8] = SUB81(uVar10,6);
    s_persist_vendor_radio_raf2_002586be[9] = SUB81(uVar10,7);
    local_140[0] = s_persist_vendor_radio_raf2_002586be[2];
    local_140[1] = s_persist_vendor_radio_raf2_002586be[3];
    local_140[2] = s_persist_vendor_radio_raf2_002586be[4];
    local_140[3] = s_persist_vendor_radio_raf2_002586be[5];
    local_140[4] = s_persist_vendor_radio_raf2_002586be[6];
    local_140[5] = s_persist_vendor_radio_raf2_002586be[7];
    local_140[6] = s_persist_vendor_radio_raf2_002586be[8];
    local_140[7] = s_persist_vendor_radio_raf2_002586be[9];
    uStack_128 = uVar16;
    s_persist_vendor_radio_raf2_002586be[0x12] = (char)uVar14;
    s_persist_vendor_radio_raf2_002586be[0x13] = SUB81(uVar14,1);
    s_persist_vendor_radio_raf2_002586be[0x14] = SUB81(uVar14,2);
    s_persist_vendor_radio_raf2_002586be[0x15] = SUB81(uVar14,3);
    s_persist_vendor_radio_raf2_002586be[0x16] = SUB81(uVar14,4);
    s_persist_vendor_radio_raf2_002586be[0x17] = SUB81(uVar14,5);
    s_persist_vendor_radio_raf2_002586be[0x18] = SUB81(uVar14,6);
    s_persist_vendor_radio_raf2_002586be[0x19] = SUB81(uVar14,7);
    acStack_130[0] = s_persist_vendor_radio_raf2_002586be[0x12];
    acStack_130[1] = s_persist_vendor_radio_raf2_002586be[0x13];
    acStack_130[2] = s_persist_vendor_radio_raf2_002586be[0x14];
    acStack_130[3] = s_persist_vendor_radio_raf2_002586be[0x15];
    acStack_130[4] = s_persist_vendor_radio_raf2_002586be[0x16];
    acStack_130[5] = s_persist_vendor_radio_raf2_002586be[0x17];
    acStack_130[6] = s_persist_vendor_radio_raf2_002586be[0x18];
    acStack_130[7] = s_persist_vendor_radio_raf2_002586be[0x19];
    s_persist_vendor_radio_raf1_002586a0[8] = (char)uVar5;
    s_persist_vendor_radio_raf1_002586a0[9] = SUB81(uVar5,1);
    s_persist_vendor_radio_raf1_002586a0[10] = SUB81(uVar5,2);
    s_persist_vendor_radio_raf1_002586a0[0xb] = SUB81(uVar5,3);
    s_persist_vendor_radio_raf1_002586a0[0xc] = SUB81(uVar5,4);
    s_persist_vendor_radio_raf1_002586a0[0xd] = SUB81(uVar5,5);
    s_persist_vendor_radio_raf1_002586a0[0xe] = SUB81(uVar5,6);
    s_persist_vendor_radio_raf1_002586a0[0xf] = SUB81(uVar5,7);
    uStack_158._0_1_ = s_persist_vendor_radio_raf1_002586a0[8];
    uStack_158._1_1_ = s_persist_vendor_radio_raf1_002586a0[9];
    uStack_158._2_1_ = s_persist_vendor_radio_raf1_002586a0[10];
    uStack_158._3_1_ = s_persist_vendor_radio_raf1_002586a0[0xb];
    uStack_158._4_1_ = s_persist_vendor_radio_raf1_002586a0[0xc];
    uStack_158._5_1_ = s_persist_vendor_radio_raf1_002586a0[0xd];
    uStack_158._6_1_ = s_persist_vendor_radio_raf1_002586a0[0xe];
    uStack_158._7_1_ = s_persist_vendor_radio_raf1_002586a0[0xf];
    local_160 = (undefined *)uVar4;
    uStack_148 = uVar9;
    s_persist_vendor_radio_raf1_002586a0[0x10] = (char)uVar7;
    s_persist_vendor_radio_raf1_002586a0[0x11] = SUB81(uVar7,1);
    s_persist_vendor_radio_raf1_002586a0[0x12] = SUB81(uVar7,2);
    s_persist_vendor_radio_raf1_002586a0[0x13] = SUB81(uVar7,3);
    s_persist_vendor_radio_raf1_002586a0[0x14] = SUB81(uVar7,4);
    s_persist_vendor_radio_raf1_002586a0[0x15] = SUB81(uVar7,5);
    s_persist_vendor_radio_raf1_002586a0[0x16] = SUB81(uVar7,6);
    s_persist_vendor_radio_raf1_002586a0[0x17] = SUB81(uVar7,7);
    acStack_150[0] = s_persist_vendor_radio_raf1_002586a0[0x10];
    acStack_150[1] = s_persist_vendor_radio_raf1_002586a0[0x11];
    acStack_150[2] = s_persist_vendor_radio_raf1_002586a0[0x12];
    acStack_150[3] = s_persist_vendor_radio_raf1_002586a0[0x13];
    acStack_150[4] = s_persist_vendor_radio_raf1_002586a0[0x14];
    acStack_150[5] = s_persist_vendor_radio_raf1_002586a0[0x15];
    acStack_150[6] = s_persist_vendor_radio_raf1_002586a0[0x16];
    acStack_150[7] = s_persist_vendor_radio_raf1_002586a0[0x17];
    uStack_8c = 0;
    uStack_90 = 0;
    uStack_c8 = 0;
    local_d0 = 0;
    uStack_b8 = 0;
    uStack_c0 = 0;
    uStack_a8 = 0;
    local_b0 = 0;
    uStack_98 = 0;
    local_94 = 0;
    uStack_a0 = 0;
    uStack_d8 = 0;
    local_e0 = (long *)0x0;
    s_persist_vendor_radio_raf1_002586a0._8_8_ = uVar6;
    s_persist_vendor_radio_raf1_002586a0._16_8_ = uVar8;
    s_persist_vendor_radio_raf2_002586be._2_8_ = uVar11;
    s_persist_vendor_radio_raf2_002586be._10_8_ = uVar13;
    s_persist_vendor_radio_raf2_002586be._18_8_ = uVar15;
    s_persist_vendor_radio_raf3_002586dc._4_8_ = uVar18;
    s_persist_vendor_radio_raf3_002586dc._12_8_ = uVar20;
    s_persist_vendor_radio_raf3_002586dc._20_8_ = uVar22;
    s_persist_vendor_radio_raf4_002586fa._6_8_ = uVar25;
    s_persist_vendor_radio_raf4_002586fa._14_8_ = uVar27;
    rfx_property_get((long)&local_160 + (long)*(int *)(this + 0x40) * 0x1e,&local_e0,&DAT_001f9509);
    lVar34 = strtol((char *)&local_e0,(char **)0x0,10);
    if (((int)lVar34 != -1) && (((uVar32 & 0x31f0) == 0) != ((uVar33 & 0x31f0) != 0))) {
      RfxController::logD((char *)this,"RtcRatSwCtrl",
                          "Skip switch if C2K no change, slotCapability:%d PreviousRaf:%d ",
                          uVar32 & 0xffffffff,uVar33 & 0xffffffff);
      iVar2 = *(int *)(this + 0x40);
      pRVar31 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,iVar2);
      iVar29 = RfxStatusManager::getIntValue(pRVar31,0x2d,0);
      setSlotCapability(this,iVar2,iVar29);
      RfxSignal2<int,int>::emit((RfxSignal2<int,int> *)(this + 0x48),*(int *)(this + 0x40),0);
      goto LAB_003ab0d0;
    }
  }
  iVar2 = *(int *)(this + 0x40);
  pRVar31 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,iVar2);
  RfxStatusManager::getIntValue(pRVar31,0x2d,0);
  local_f0 = 0x346661;
  acStack_118[0] = s_persist_vendor_radio_raf3_002586dc[0xc];
  acStack_118[1] = s_persist_vendor_radio_raf3_002586dc[0xd];
  acStack_118[2] = s_persist_vendor_radio_raf3_002586dc[0xe];
  acStack_118[3] = s_persist_vendor_radio_raf3_002586dc[0xf];
  acStack_118[4] = s_persist_vendor_radio_raf3_002586dc[0x10];
  acStack_118[5] = s_persist_vendor_radio_raf3_002586dc[0x11];
  acStack_118[6] = s_persist_vendor_radio_raf3_002586dc[0x12];
  acStack_118[7] = s_persist_vendor_radio_raf3_002586dc[0x13];
  local_120[0] = s_persist_vendor_radio_raf3_002586dc[4];
  local_120[1] = s_persist_vendor_radio_raf3_002586dc[5];
  local_120[2] = s_persist_vendor_radio_raf3_002586dc[6];
  local_120[3] = s_persist_vendor_radio_raf3_002586dc[7];
  local_120[4] = s_persist_vendor_radio_raf3_002586dc[8];
  local_120[5] = s_persist_vendor_radio_raf3_002586dc[9];
  local_120[6] = s_persist_vendor_radio_raf3_002586dc[10];
  local_120[7] = s_persist_vendor_radio_raf3_002586dc[0xb];
  uStack_108 = _UNK_002586f8;
  acStack_110[0] = s_persist_vendor_radio_raf3_002586dc[0x14];
  acStack_110[1] = s_persist_vendor_radio_raf3_002586dc[0x15];
  acStack_110[2] = s_persist_vendor_radio_raf3_002586dc[0x16];
  acStack_110[3] = s_persist_vendor_radio_raf3_002586dc[0x17];
  acStack_110[4] = s_persist_vendor_radio_raf3_002586dc[0x18];
  acStack_110[5] = s_persist_vendor_radio_raf3_002586dc[0x19];
  acStack_110[6] = s_persist_vendor_radio_raf3_002586dc[0x1a];
  acStack_110[7] = s_persist_vendor_radio_raf3_002586dc[0x1b];
  acStack_f8[0] = s_persist_vendor_radio_raf4_002586fa[0xe];
  acStack_f8[1] = s_persist_vendor_radio_raf4_002586fa[0xf];
  acStack_f8[2] = s_persist_vendor_radio_raf4_002586fa[0x10];
  acStack_f8[3] = s_persist_vendor_radio_raf4_002586fa[0x11];
  acStack_f8[4] = s_persist_vendor_radio_raf4_002586fa[0x12];
  acStack_f8[5] = s_persist_vendor_radio_raf4_002586fa[0x13];
  acStack_f8[6] = s_persist_vendor_radio_raf4_002586fa[0x14];
  acStack_f8[7] = s_persist_vendor_radio_raf4_002586fa[0x15];
  local_100[0] = s_persist_vendor_radio_raf4_002586fa[6];
  local_100[1] = s_persist_vendor_radio_raf4_002586fa[7];
  local_100[2] = s_persist_vendor_radio_raf4_002586fa[8];
  local_100[3] = s_persist_vendor_radio_raf4_002586fa[9];
  local_100[4] = s_persist_vendor_radio_raf4_002586fa[10];
  local_100[5] = s_persist_vendor_radio_raf4_002586fa[0xb];
  local_100[6] = s_persist_vendor_radio_raf4_002586fa[0xc];
  local_100[7] = s_persist_vendor_radio_raf4_002586fa[0xd];
  uStack_158._0_1_ = s_persist_vendor_radio_raf1_002586a0[8];
  uStack_158._1_1_ = s_persist_vendor_radio_raf1_002586a0[9];
  uStack_158._2_1_ = s_persist_vendor_radio_raf1_002586a0[10];
  uStack_158._3_1_ = s_persist_vendor_radio_raf1_002586a0[0xb];
  uStack_158._4_1_ = s_persist_vendor_radio_raf1_002586a0[0xc];
  uStack_158._5_1_ = s_persist_vendor_radio_raf1_002586a0[0xd];
  uStack_158._6_1_ = s_persist_vendor_radio_raf1_002586a0[0xe];
  uStack_158._7_1_ = s_persist_vendor_radio_raf1_002586a0[0xf];
  local_160 = (undefined *)s_persist_vendor_radio_raf1_002586a0._0_8_;
  uStack_148 = ram0x002586b8;
  acStack_150[0] = s_persist_vendor_radio_raf1_002586a0[0x10];
  acStack_150[1] = s_persist_vendor_radio_raf1_002586a0[0x11];
  acStack_150[2] = s_persist_vendor_radio_raf1_002586a0[0x12];
  acStack_150[3] = s_persist_vendor_radio_raf1_002586a0[0x13];
  acStack_150[4] = s_persist_vendor_radio_raf1_002586a0[0x14];
  acStack_150[5] = s_persist_vendor_radio_raf1_002586a0[0x15];
  acStack_150[6] = s_persist_vendor_radio_raf1_002586a0[0x16];
  acStack_150[7] = s_persist_vendor_radio_raf1_002586a0[0x17];
  uStack_8c = 0;
  uStack_90 = 0;
  uStack_c8 = 0;
  local_d0 = 0;
  uStack_b8 = 0;
  uStack_c0 = 0;
  uStack_a8 = 0;
  local_b0 = 0;
  uStack_98 = 0;
  local_94 = 0;
  uStack_a0 = 0;
  uStack_d8 = 0;
  local_e0 = (long *)0x0;
  acStack_138[0] = s_persist_vendor_radio_raf2_002586be[10];
  acStack_138[1] = s_persist_vendor_radio_raf2_002586be[0xb];
  acStack_138[2] = s_persist_vendor_radio_raf2_002586be[0xc];
  acStack_138[3] = s_persist_vendor_radio_raf2_002586be[0xd];
  acStack_138[4] = s_persist_vendor_radio_raf2_002586be[0xe];
  acStack_138[5] = s_persist_vendor_radio_raf2_002586be[0xf];
  acStack_138[6] = s_persist_vendor_radio_raf2_002586be[0x10];
  acStack_138[7] = s_persist_vendor_radio_raf2_002586be[0x11];
  local_140[0] = s_persist_vendor_radio_raf2_002586be[2];
  local_140[1] = s_persist_vendor_radio_raf2_002586be[3];
  local_140[2] = s_persist_vendor_radio_raf2_002586be[4];
  local_140[3] = s_persist_vendor_radio_raf2_002586be[5];
  local_140[4] = s_persist_vendor_radio_raf2_002586be[6];
  local_140[5] = s_persist_vendor_radio_raf2_002586be[7];
  local_140[6] = s_persist_vendor_radio_raf2_002586be[8];
  local_140[7] = s_persist_vendor_radio_raf2_002586be[9];
  uStack_128 = _UNK_002586d8;
  acStack_130[0] = s_persist_vendor_radio_raf2_002586be[0x12];
  acStack_130[1] = s_persist_vendor_radio_raf2_002586be[0x13];
  acStack_130[2] = s_persist_vendor_radio_raf2_002586be[0x14];
  acStack_130[3] = s_persist_vendor_radio_raf2_002586be[0x15];
  acStack_130[4] = s_persist_vendor_radio_raf2_002586be[0x16];
  acStack_130[5] = s_persist_vendor_radio_raf2_002586be[0x17];
  acStack_130[6] = s_persist_vendor_radio_raf2_002586be[0x18];
  acStack_130[7] = s_persist_vendor_radio_raf2_002586be[0x19];
  iVar29 = sprintf((char *)&local_e0,extraout_x1);
  if (iVar29 < 0) {
    RfxController::logE((char *)this,"RtcRatSwCtrl","setIntSysProp sprintf fail");
  }
  rfx_property_set((long)&local_160 + (long)iVar2 * 0x1e,&local_e0);
  *(uint *)(this + 0x7c) = uVar28;
  uVar30 = getPreferredNetWorkTypeFromSysProp(this,*(int *)(this + 0x40));
  if (((char)param_2[2] != '\0') && ((param_2[1] == 3 || (param_2[1] == 0)))) {
    uVar30 = 0xbfffe;
  }
  uVar28 = uVar30 & uVar28;
  if (uVar28 == 0) {
    RfxController::logD((char *)this,"RtcRatSwCtrl",
                        "[setPreferredNetworkType] Raf filter result is 0, so use defaultRaf as targetRaf"
                       );
    uVar28 = 0x10006;
    if ((char)param_2[2] == '\0') goto LAB_003ab010;
LAB_003aaddc:
    uVar35 = uVar28 & 0x31f0;
    if ((uVar35 == 0) || (param_2[1] != 4)) goto LAB_003ab018;
    if ((param_2[4] == 4) || (param_2[4] != 2)) goto LAB_003ab01c;
LAB_003aae08:
    *(undefined4 *)(this + 0x104) = 1;
  }
  else {
    if ((char)param_2[2] != '\0') goto LAB_003aaddc;
LAB_003ab010:
    uVar35 = uVar28 & 0x31f0;
LAB_003ab018:
    if (uVar35 == 0) goto LAB_003aae08;
LAB_003ab01c:
    *(undefined4 *)(this + 0x104) = 0;
  }
  pRVar31 = (RfxStatusManager *)
            RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
  local_160 = (undefined *)CONCAT44(local_160._4_4_,2);
  uStack_158 = CONCAT44(uStack_158._4_4_,*(undefined4 *)(this + 0x104));
  RfxStatusManager::setValue(pRVar31,0x58,(RfxVariant *)&local_160,false,false);
  RfxVariant::deinit((RfxVariant *)&local_160);
  RfxController::logD((char *)this,"RtcRatSwCtrl",
                      "[setPreferredNetworkType] rafFromRiljNetworkType: %d, targetRaf: %d, mNwsMode: %d, mInRestrictedMode: %d"
                      ,(ulong)uVar30,(ulong)uVar28,(ulong)*(uint *)(this + 0x104),
                      (ulong)(byte)this[0x110]);
  if (this[0x110] != (RtcRatSwitchController)0x0) {
    this[0x110] = (RtcRatSwitchController)0x0;
    *(undefined4 *)(this + 0xe0) = 0xffffffff;
  }
  local_160 = (undefined *)0x0;
  local_e0 = (long *)0x0;
  switchNwRat(this,uVar28,0,&local_160,&local_e0);
  if (local_e0 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_e0 + *(long *)(*local_e0 + -0x18)),&local_e0);
  }
LAB_003ab0d0:
  if (*(long *)(lVar3 + 0x28) == local_78) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


