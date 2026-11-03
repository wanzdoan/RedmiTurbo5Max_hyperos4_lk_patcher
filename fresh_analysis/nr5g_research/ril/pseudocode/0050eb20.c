
/* RmcDcCommonReqHandler::sendQualifiedNetworkTypesChanged(int) */

void __thiscall
RmcDcCommonReqHandler::sendQualifiedNetworkTypesChanged(RmcDcCommonReqHandler *this,int param_1)

{
  long lVar1;
  RfxAtResponse *pRVar2;
  int iVar3;
  undefined4 uVar4;
  ulong uVar5;
  char *pcVar6;
  ulong uVar7;
  RfxAtLine *this_00;
  long *local_700;
  long *local_6f8;
  int local_6ec;
  RfxAtResponse *local_6e8;
  undefined8 local_6e0;
  RfxAtResponse *local_6d8 [2];
  int local_6c8;
  int local_6c0;
  int local_9c;
  undefined8 local_58;
  int local_50;
  long local_48;
  
  lVar1 = tpidr_el0;
  uVar7 = (ulong)(uint)param_1;
  local_48 = *(long *)(lVar1 + 0x28);
  local_6e0 = 0;
  android::String8::String8((String8 *)&local_6e0,"");
  local_6e8 = (RfxAtResponse *)0x0;
  local_6ec = 0;
  local_50 = 0;
  local_58 = 0;
  local_6f8 = (long *)0x0;
  android::String8::format("AT+EPDN=%d,\"ran\"",local_6d8,uVar7);
  android::String8::append((String8 *)&local_6e0);
  android::String8::~String8((String8 *)local_6d8);
  RfxBaseHandler::atSendCommandSingleline((String8 *)this,(char *)&local_6e0,"+EPDN:");
  pRVar2 = local_6e8;
  if (local_6e8 != (RfxAtResponse *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)(local_6e8 + *(long *)(*(long *)local_6e8 + -0x18)),&local_6e8);
  }
  if (pRVar2 != local_6e8) {
    android::sp_report_race();
  }
  local_6e8 = local_6d8[0];
  if (local_6d8[0] == (RfxAtResponse *)0x0) {
    uVar7 = __rfx_is_gt_mode();
    if ((uVar7 & 1) == 0) {
      uVar4 = *(undefined4 *)(this + 8);
      pcVar6 = "[%d][%s] fail to get p_response!";
      goto LAB_0050ef04;
    }
    local_6d8[0] = (RfxAtResponse *)0x0;
    android::String8::format("%s%s",local_6d8,&DAT_001fbebf,"RmcDcCommon");
    pcVar6 = "[%d][%s] fail to get p_response!";
LAB_0050ee60:
    mtkLogE(local_6d8[0],pcVar6,*(undefined4 *)(this + 8),"sendQualifiedNetworkTypesChanged");
  }
  else {
    uVar5 = RfxAtResponse::isAtResponseFail(local_6d8[0]);
    if ((uVar5 & 1) == 0) {
      this_00 = *(RfxAtLine **)(local_6e8 + 0x20);
      if (this_00 == (RfxAtLine *)0x0) {
        uVar7 = __rfx_is_gt_mode();
        if ((uVar7 & 1) == 0) {
          uVar4 = *(undefined4 *)(this + 8);
          pcVar6 = "[%d][%s] fail to get intermediate results!";
          goto LAB_0050ef04;
        }
        local_6d8[0] = (RfxAtResponse *)0x0;
        android::String8::format("%s%s",local_6d8,&DAT_001fbebf,"RmcDcCommon");
        pcVar6 = "[%d][%s] fail to get intermediate results!";
      }
      else {
        RfxAtLine::atTokStart(this_00,&local_6ec);
        if (local_6ec < 0) {
          uVar7 = __rfx_is_gt_mode();
          if ((uVar7 & 1) == 0) {
            uVar4 = *(undefined4 *)(this + 8);
            pcVar6 = "[%d][%s] ERROR occurs when token start";
            goto LAB_0050ef04;
          }
          local_6d8[0] = (RfxAtResponse *)0x0;
          android::String8::format("%s%s",local_6d8,&DAT_001fbebf,"RmcDcCommon");
          pcVar6 = "[%d][%s] ERROR occurs when token start";
        }
        else {
          iVar3 = RfxAtLine::atTokNextint(this_00,&local_6ec);
          if ((iVar3 == param_1) && (-1 < local_6ec)) {
            pcVar6 = (char *)RfxAtLine::atTokNextstr(this_00,&local_6ec);
            if (-1 < local_6ec) {
              iVar3 = strncmp("ran",pcVar6,3);
              if (iVar3 == 0) {
                iVar3 = RfxAtLine::atTokNextint(this_00,&local_6ec);
                if ((0 < iVar3) && (-1 < local_6ec)) {
                  getPdnInfo((int)this);
                  if ((local_6c8 != 0) || (local_6c0 != 2)) goto LAB_0050ef10;
                  local_58 = CONCAT44(local_58._4_4_,1);
                  uVar4 = RmcDcUtility::getApnTypeId(local_9c);
                  local_58 = CONCAT44(uVar4,(int)local_58);
                  if (iVar3 - 3U < 4) {
                    uVar5 = __rfx_is_gt_mode();
                    if ((uVar5 & 1) == 0) {
                      mtkLogI("RmcDcCommon","[%d][%s] rat=%d change to IWLAN(2)",
                              *(undefined4 *)(this + 8),"sendQualifiedNetworkTypesChanged",iVar3);
                    }
                    else {
                      local_6d8[0] = (RfxAtResponse *)0x0;
                      android::String8::format("%s%s",local_6d8,&DAT_001fbebf,"RmcDcCommon");
                      mtkLogI(local_6d8[0],"[%d][%s] rat=%d change to IWLAN(2)",
                              *(undefined4 *)(this + 8),"sendQualifiedNetworkTypesChanged",iVar3);
                      android::String8::~String8((String8 *)local_6d8);
                    }
                    iVar3 = 2;
                  }
                  local_50 = iVar3;
                  uVar5 = __rfx_is_gt_mode();
                  if ((uVar5 & 1) == 0) {
                    pcVar6 = "sendQualifiedNetworkTypesChanged";
                    mtkLogI("RmcDcCommon","[%d][%s] for xfrm use aid=%d, rat=%d",
                            *(undefined4 *)(this + 8),"sendQualifiedNetworkTypesChanged",param_1,
                            iVar3);
                  }
                  else {
                    local_6d8[0] = (RfxAtResponse *)0x0;
                    android::String8::format("%s%s",local_6d8,&DAT_001fbebf,"RmcDcCommon");
                    pcVar6 = "sendQualifiedNetworkTypesChanged";
                    mtkLogI(local_6d8[0],"[%d][%s] for xfrm use aid=%d, rat=%d",
                            *(undefined4 *)(this + 8),"sendQualifiedNetworkTypesChanged",uVar7,iVar3
                           );
                    android::String8::~String8((String8 *)local_6d8);
                  }
                  updateRat(this,param_1,iVar3);
                  iVar3 = *(int *)(this + 8);
                  RfxIntsData::RfxIntsData((RfxIntsData *)local_6d8,(int *)&local_58,3);
                  RfxMclMessage::obtainUrc
                            ((RfxMclMessage *)0xcd49,iVar3,(int)local_6d8,(RfxBaseData *)pcVar6);
                  android::sp<RfxMclMessage>::operator=
                            ((sp<RfxMclMessage> *)&local_6f8,(sp *)&local_700);
                  if (local_700 != (long *)0x0) {
                    android::RefBase::decStrong
                              ((RefBase *)((long)local_700 + *(long *)(*local_700 + -0x18)),
                               &local_700);
                  }
                  RfxIntsData::~RfxIntsData((RfxIntsData *)local_6d8);
                  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_6f8);
                  uVar7 = __rfx_is_gt_mode();
                  if ((uVar7 & 1) == 0) {
                    mtkLogI("RmcDcCommon","[%d][%s] mode=%d, apnType=%d, rat=%d",
                            *(undefined4 *)(this + 8),"sendQualifiedNetworkTypesChanged",
                            local_58 & 0xffffffff,local_58._4_4_,local_50);
                    goto LAB_0050ef10;
                  }
                  local_6d8[0] = (RfxAtResponse *)0x0;
                  android::String8::format("%s%s",local_6d8,&DAT_001fbebf,"RmcDcCommon");
                  mtkLogI(local_6d8[0],"[%d][%s] mode=%d, apnType=%d, rat=%d",
                          *(undefined4 *)(this + 8),"sendQualifiedNetworkTypesChanged",
                          local_58 & 0xffffffff,local_58._4_4_,local_50);
                  goto LAB_0050ee70;
                }
                uVar7 = __rfx_is_gt_mode();
                if ((uVar7 & 1) == 0) {
                  uVar4 = *(undefined4 *)(this + 8);
                  pcVar6 = "[%d][%s] fail to get rat or it\'s value not greater or equal to 1";
                  goto LAB_0050ef04;
                }
                local_6d8[0] = (RfxAtResponse *)0x0;
                android::String8::format("%s%s",local_6d8,&DAT_001fbebf,"RmcDcCommon");
                pcVar6 = "[%d][%s] fail to get rat or it\'s value not greater or equal to 1";
                goto LAB_0050ee60;
              }
            }
            uVar7 = __rfx_is_gt_mode();
            if ((uVar7 & 1) == 0) {
              uVar4 = *(undefined4 *)(this + 8);
              pcVar6 = "[%d][%s] ERROR occurs when parsing cmd";
LAB_0050ef04:
              mtkLogE("RmcDcCommon",pcVar6,uVar4,"sendQualifiedNetworkTypesChanged");
              goto LAB_0050ef10;
            }
            local_6d8[0] = (RfxAtResponse *)0x0;
            android::String8::format("%s%s",local_6d8,&DAT_001fbebf,"RmcDcCommon");
            pcVar6 = "[%d][%s] ERROR occurs when parsing cmd";
          }
          else {
            uVar7 = __rfx_is_gt_mode();
            if ((uVar7 & 1) == 0) {
              uVar4 = *(undefined4 *)(this + 8);
              pcVar6 = "[%d][%s] fail to get aid or it\'s different to MD assigned";
              goto LAB_0050ef04;
            }
            local_6d8[0] = (RfxAtResponse *)0x0;
            android::String8::format("%s%s",local_6d8,&DAT_001fbebf,"RmcDcCommon");
            pcVar6 = "[%d][%s] fail to get aid or it\'s different to MD assigned";
          }
        }
      }
      goto LAB_0050ee60;
    }
    if (0 < *(int *)(local_6e8 + 0xc)) {
      uVar7 = __rfx_is_gt_mode();
      if ((uVar7 & 1) == 0) {
        uVar4 = *(undefined4 *)(this + 8);
        pcVar6 = 
        "[%d][%s] weird case, can not get fail cause due to final response indicates success";
        goto LAB_0050ef04;
      }
      local_6d8[0] = (RfxAtResponse *)0x0;
      android::String8::format("%s%s",local_6d8,&DAT_001fbebf,"RmcDcCommon");
      pcVar6 = "[%d][%s] weird case, can not get fail cause due to final response indicates success"
      ;
      goto LAB_0050ee60;
    }
    uVar4 = RfxAtResponse::atGetCmeError(local_6e8);
    uVar5 = __rfx_is_gt_mode();
    if ((uVar5 & 1) == 0) {
      mtkLogE("RmcDcCommon","[%d][%s] AT+EPDN=%d,\"ran\" returns cme cause : %d",
              *(undefined4 *)(this + 8),"sendQualifiedNetworkTypesChanged",uVar7,uVar4);
      goto LAB_0050ef10;
    }
    local_6d8[0] = (RfxAtResponse *)0x0;
    android::String8::format("%s%s",local_6d8,&DAT_001fbebf,"RmcDcCommon");
    mtkLogE(local_6d8[0],"[%d][%s] AT+EPDN=%d,\"ran\" returns cme cause : %d",
            *(undefined4 *)(this + 8),"sendQualifiedNetworkTypesChanged",uVar7,uVar4);
  }
LAB_0050ee70:
  android::String8::~String8((String8 *)local_6d8);
LAB_0050ef10:
  if (local_6f8 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_6f8 + *(long *)(*local_6f8 + -0x18)),&local_6f8);
  }
  if (local_6e8 != (RfxAtResponse *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)(local_6e8 + *(long *)(*(long *)local_6e8 + -0x18)),&local_6e8);
  }
  android::String8::~String8((String8 *)&local_6e0);
  if (*(long *)(lVar1 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


