
/* RtcRatSwitchController::handleGmssRatForNrExtend(android::sp<RfxMessage> const&) */

void __thiscall
RtcRatSwitchController::handleGmssRatForNrExtend(RtcRatSwitchController *this,sp *param_1)

{
  uint uVar1;
  long lVar2;
  uint uVar3;
  RfxStatusManager *pRVar4;
  ulong uVar5;
  undefined *puVar6;
  uint uVar7;
  ulong *puVar8;
  ushort uVar9;
  ulong uVar10;
  uint uVar11;
  int iVar12;
  ulong uVar13;
  uint uVar14;
  undefined8 local_c0;
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
  
  lVar2 = tpidr_el0;
  local_58 = *(long *)(lVar2 + 0x28);
  puVar8 = *(ulong **)(*(long *)(*(long *)param_1 + 0x48) + 8);
  uVar13 = puVar8[1];
  uVar10 = *puVar8;
  uVar1 = (uint)puVar8[2];
  uVar11 = (uint)(uVar10 >> 0x20);
  iVar12 = (int)uVar13;
  uVar14 = (uint)(uVar13 >> 0x20);
  uVar9 = NEON_umaxv(CONCAT26(-(ushort)(uVar14 != 0),
                              CONCAT24(-(ushort)(iVar12 != 0),
                                       CONCAT22(-(ushort)(uVar11 != 0),-(ushort)((int)uVar10 != 0)))
                             ),2);
  if (((uVar9 & 1) == 0) && (uVar1 == 0)) goto LAB_003ad7bc;
  if (iVar12 == 2) {
    if (((uVar10 & 0x60) != 0) ||
       ((((uVar10 & 3) == 0 && (uVar1 != 0)) &&
        ((uVar5 = isCdmaDualModeSimCard(this), (uVar5 & 1) != 0 &&
         (uVar5 = RtcCapabilitySwitchUtil::isDisableC2kCapability(), (uVar5 & 1) == 0))))))
    goto LAB_003ad6c8;
LAB_003ad71c:
    uVar7 = 1;
LAB_003ad724:
    uVar3 = 0;
    puVar6 = &DAT_00235024;
    *(uint *)(this + 0x104) = uVar7;
  }
  else {
    if ((uVar14 >> 4 & 1) == 0) goto LAB_003ad71c;
    if ((uVar13 & 0x300000000) != 0) {
      if (iVar12 != 1) goto LAB_003ad6d0;
      if ((uVar10 & 0x60) != 0) goto LAB_003ad6c8;
      if ((uVar10 & 3) != 0) goto LAB_003ad71c;
      uVar7 = (uint)(uVar1 == 0);
      goto LAB_003ad724;
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
    local_c0 = 0;
    rfx_property_get("persist.vendor.mtk_ct_volte_support",&local_c0,&DAT_00235024);
    if (((((short)local_c0 == 0x33) && (uVar11 == 0x1cc)) &&
        ((*(uint *)(this + 0xfc) & 0x31f0) != 0)) && ((*(uint *)(this + 0xfc) & 0x1bce0e) != 0)) {
      pRVar4 = (RfxStatusManager *)
               RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
      uVar3 = RfxStatusManager::getIntValue(pRVar4,0x97,0);
      if ((uVar3 & 1) == 0) {
        *(undefined4 *)(this + 0x104) = 0;
      }
      else {
        *(undefined4 *)(this + 0x104) = 1;
      }
      puVar6 = &DAT_002240c5;
    }
    else {
LAB_003ad6c8:
      *(undefined4 *)(this + 0x104) = 0;
LAB_003ad6d0:
      uVar3 = 0;
      puVar6 = &DAT_00235024;
    }
  }
  setMSimProperty(*(undefined4 *)(this + 0x40),"vendor.ril.mtk_hvolte_indicator",puVar6);
  pRVar4 = (RfxStatusManager *)
           RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
  local_c0 = CONCAT44(local_c0._4_4_,2);
  uStack_b8 = CONCAT44(uStack_b8._4_4_,*(undefined4 *)(this + 0x104));
  RfxStatusManager::setValue(pRVar4,0x58,(RfxVariant *)&local_c0,false,false);
  RfxVariant::deinit((RfxVariant *)&local_c0);
  RfxController::logD((char *)this,"RtcRatSwCtrl",
                      "[handleGmssRatForNrExtend] rat:%d, mcc:%d, status:%d, mspl:%d, ishome:%d, NWS mode:%d,volteRegistration: %d."
                      ,uVar10 & 0xffffffff,(ulong)uVar11,uVar13 & 0xffffffff,(ulong)uVar14,
                      (ulong)uVar1,*(undefined4 *)(this + 0x104),uVar3);
LAB_003ad7bc:
  if (*(long *)(lVar2 + 0x28) != local_58) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}


