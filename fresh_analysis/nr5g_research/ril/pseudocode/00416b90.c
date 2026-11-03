
/* RtcSbpController::onSbpIdChanged(int, unsigned int, RfxVariant, RfxVariant) */

void RtcSbpController::onSbpIdChanged(RfxController *param_1,uint param_2)

{
  long lVar1;
  char *pcVar2;
  uint uVar3;
  int iVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  uint uVar8;
  RfxStatusManager *pRVar9;
  String8 *pSVar10;
  undefined8 *puVar11;
  char *pcVar12;
  ulong uVar13;
  String8 aSStack_d0 [8];
  char *local_c8;
  char *local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined4 uStack_78;
  undefined4 local_74;
  undefined4 uStack_70;
  undefined8 uStack_6c;
  long local_58;
  
  lVar1 = tpidr_el0;
  local_58 = *(long *)(lVar1 + 0x28);
  uStack_6c = 0;
  uStack_70 = 0;
  uStack_a8 = 0;
  local_b0 = 0;
  uStack_98 = 0;
  uStack_a0 = 0;
  uStack_88 = 0;
  local_90 = 0;
  uStack_78 = 0;
  local_74 = 0;
  uStack_80 = 0;
  uStack_b8 = 0;
  local_c0 = (char *)0x0;
  rfx_property_get("ro.vendor.ril.set_sbp_place",&local_c0,&DAT_00235024);
  uVar3 = atoi((char *)&local_c0);
  uVar13 = (ulong)uVar3;
  if ((uVar3 == 3) || (uVar3 == 0)) {
    pcVar12 = "onSbpIdChanged[%d] not support, setter:%d";
  }
  else {
    iVar4 = RfxController::getFeatureVersion(param_1,"NA_NO_CXP_REBOOT");
    if ((uVar3 == 2) && (iVar4 == 1)) {
      RfxController::logI((char *)param_1,"RtcSbp",
                          "onSbpIdChanged[%d] not support, setter:%d, mdver:%d",(ulong)param_2,2,1);
      goto LAB_00416c24;
    }
    uStack_6c = 0;
    uStack_70 = 0;
    uStack_a8 = 0;
    local_b0 = 0;
    uStack_98 = 0;
    uStack_a0 = 0;
    uStack_88 = 0;
    local_90 = 0;
    uStack_78 = 0;
    local_74 = 0;
    uStack_80 = 0;
    uStack_b8 = 0;
    local_c0 = (char *)0x0;
    rfx_property_get("persist.vendor.mtk_usp_switch_mode",&local_c0,&DAT_002240c5);
    uVar3 = atoi((char *)&local_c0);
    uVar13 = (ulong)uVar3;
    if (0xfffffffd < uVar3 - 3) {
      if (uVar3 == 2) {
        uStack_6c = 0;
        uStack_70 = 0;
        uStack_a8 = 0;
        local_b0 = 0;
        uStack_98 = 0;
        uStack_a0 = 0;
        uStack_88 = 0;
        local_90 = 0;
        uStack_78 = 0;
        local_74 = 0;
        uStack_80 = 0;
        uStack_b8 = 0;
        local_c0 = (char *)0x0;
        rfx_property_get("persist.vendor.radio.sbp_flag",&local_c0,&DAT_00235024);
        iVar4 = atoi((char *)&local_c0);
        if (iVar4 == 1) {
          pcVar12 = "onSbpIdChanged[%d], already checked first sim, switch_mode:%d,";
          uVar13 = 2;
          goto LAB_00416c20;
        }
      }
      pRVar9 = (RfxStatusManager *)RfxController::getStatusManager(param_1,-1);
      uVar3 = RfxStatusManager::getIntValue(pRVar9,0x34,0);
      if (uVar3 == param_2) {
        local_c0 = (char *)0x0;
        pRVar9 = (RfxStatusManager *)RfxController::getStatusManager(param_1,param_2);
        android::String8::String8((String8 *)&local_c8,"");
        pSVar10 = (String8 *)RfxStatusManager::getString8Value(pRVar9,0x75,(String8 *)&local_c8);
        android::String8::String8((String8 *)&local_c0,pSVar10);
        android::String8::~String8((String8 *)&local_c8);
        local_c8 = (char *)0x0;
        pRVar9 = (RfxStatusManager *)RfxController::getStatusManager(param_1,param_2);
        android::String8::String8(aSStack_d0,"");
        pSVar10 = (String8 *)RfxStatusManager::getString8Value(pRVar9,10,aSStack_d0);
        android::String8::String8((String8 *)&local_c8,pSVar10);
        android::String8::~String8(aSStack_d0);
        uVar13 = android::String8::length();
        pcVar2 = local_c0;
        pcVar12 = local_c8;
        if ((uVar13 < 2) || (iVar4 = strcmp(local_c0,local_c8), iVar4 != 0)) {
          RfxController::logD((char *)param_1,"RtcSbp",
                              "onSbpIdChanged[%d], major sim[%d] config_mccmnc[%s], usim_mccmnc[%s] invalid"
                              ,(ulong)param_2,(ulong)param_2,pcVar2,pcVar12);
        }
        else {
          uVar3 = getCurrentModemSbp();
          uVar5 = getCurrentSubId();
          pRVar9 = (RfxStatusManager *)RfxController::getStatusManager(param_1,param_2);
          puVar11 = (undefined8 *)RfxStatusManager::getString8Value(pRVar9,0xc4);
          uVar6 = atoi((char *)*puVar11);
          uVar7 = getRefinedSbpId((RtcSbpController *)(ulong)uVar6,uVar6);
          pRVar9 = (RfxStatusManager *)RfxController::getStatusManager(param_1,param_2);
          puVar11 = (undefined8 *)RfxStatusManager::getString8Value(pRVar9,0xc5);
          uVar8 = atoi((char *)*puVar11);
          RfxController::logD((char *)param_1,"RtcSbp",
                              "onSbpIdChanged[%d], cur_sbp:%d, cur_sub:%d, config_sbp:%d, sbp_id:%d, sub_id:%d"
                              ,(ulong)param_2,(ulong)uVar3,(ulong)uVar5,
                              (RtcSbpController *)(ulong)uVar6,(ulong)uVar7,uVar8);
          if ((uVar3 == uVar7) && (uVar5 == uVar8)) {
            rfx_property_set("persist.vendor.radio.sbp_flag",&DAT_002240c5);
          }
          else {
            switchSbp((RtcSbpController *)param_1,uVar7,uVar8);
          }
        }
        android::String8::~String8((String8 *)&local_c8);
        android::String8::~String8((String8 *)&local_c0);
      }
      else {
        RfxController::logD((char *)param_1,"RtcSbp",
                            "onSbpIdChanged, slot(%d) not major SIM, major_slot:%d",(ulong)param_2,
                            (ulong)uVar3);
      }
      goto LAB_00416c24;
    }
    pcVar12 = "onSbpIdChanged[%d], don\'t set because switch_mode:%d";
  }
LAB_00416c20:
  RfxController::logI((char *)param_1,"RtcSbp",pcVar12,(ulong)param_2,uVar13);
LAB_00416c24:
  if (*(long *)(lVar1 + 0x28) == local_58) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


